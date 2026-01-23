package com.github.terrakok.nedleraar.ui.question

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import com.russhwolf.settings.Settings
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class OpenQuestionStateStore @AssistedInject constructor(
    @Assisted private val lessonId: String,
    private val settings: Settings,
    private val json: Json,
    @Assisted private val scope: CoroutineScope,
) {
    private val answerStates = mutableMapOf<String, MutableState<String>>()
    private val storedStates = mutableMapOf<String, StoredState>()
    private val answerScope: CoroutineScope by lazy {
        val parentJob = scope.coroutineContext[Job]
        val job = SupervisorJob(parentJob)
        CoroutineScope(scope.coroutineContext + job)
    }

    @OptIn(FlowPreview::class)
    fun createAnswerState(questionId: String): MutableState<String> {
        val existing = answerStates[questionId]
        if (existing != null) return existing

        val storedState = restoreStoredState(questionId)
        val state = mutableStateOf(storedState.answer)
        answerStates[questionId] = state
        answerScope.launch {
            snapshotFlow { state.value }
                .debounce(ANSWER_SAVE_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collect { value ->
                    updateStoredState(questionId) { existingState ->
                        existingState.copy(answer = value)
                    }
                }
        }
        return state
    }

    fun restoreFeedback(questionId: String): Feedback? {
        val feedback = restoreStoredState(questionId).feedback
        return if (feedback is Feedback.Loading) null else feedback
    }

    fun saveFeedback(questionId: String, feedback: Feedback) {
        if (feedback is Feedback.Loading) return
        updateStoredState(questionId) { existing ->
            existing.copy(
                answer = answerStates[questionId]?.value ?: existing.answer,
                feedback = feedback
            )
        }
    }

    fun restoreLastQuestionIndex(totalQuestions: Int): Int {
        if (totalQuestions <= 0) return 0
        val saved = settings.getInt(lastQuestionKey(), 0)
        return saved.coerceIn(0, totalQuestions - 1)
    }

    fun saveLastQuestionIndex(index: Int) {
        settings.putInt(lastQuestionKey(), index)
    }

    private fun restoreStoredState(questionId: String): StoredState {
        if (storedStates.containsKey(questionId)) return storedStates[questionId]!!

        val storedJson = settings.getString(stateKey(questionId), "")
        val restored = if (storedJson.isBlank()) {
            StoredState()
        } else {
            runCatching { json.decodeFromString<StoredState>(storedJson) }
                .getOrElse { StoredState() }
        }
        storedStates[questionId] = restored
        return restored
    }

    private fun updateStoredState(questionId: String, transform: (StoredState) -> StoredState) {
        val existing = restoreStoredState(questionId)
        val updated = transform(existing)
        storedStates[questionId] = updated
        saveStoredState(questionId, updated)
    }

    private fun saveStoredState(questionId: String, state: StoredState) {
        settings.putString(stateKey(questionId), json.encodeToString(state))
    }

    private fun stateKey(questionId: String) = "$STATE_PREFIX.$lessonId.$questionId"
    private fun lastQuestionKey() = "$LAST_QUESTION_PREFIX.$lessonId"

    private companion object {
        private const val STATE_PREFIX = "open_question.state"
        private const val LAST_QUESTION_PREFIX = "open_question.last_index"
        private const val ANSWER_SAVE_DEBOUNCE_MS = 500L
    }

    @Serializable
    internal data class StoredState(
        val answer: String = "",
        val feedback: Feedback? = null
    )

    @AssistedFactory
    interface Factory {
        fun create(lessonId: String, scope: CoroutineScope): OpenQuestionStateStore
    }
}
