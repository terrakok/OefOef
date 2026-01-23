package com.github.terrakok.nedleraar.ui.question

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

internal class OpenQuestionStateStore(
    private val lessonId: String,
    private val settings: Settings,
    private val scope: CoroutineScope,
) {
    private val answerStates = mutableMapOf<String, MutableState<String>>()
    private var answerScopeJob: Job? = null
    private var answerScope: CoroutineScope? = null

    fun clear() {
        answerScopeJob?.cancel()
        answerScopeJob = null
        answerScope = null
        answerStates.clear()
    }

    @OptIn(FlowPreview::class)
    fun createAnswerState(questionId: String): MutableState<String> {
        val existing = answerStates[questionId]
        if (existing != null) return existing

        val settingsKey = answerKey(questionId)
        val state = mutableStateOf(settings.getString(settingsKey, ""))
        answerStates[questionId] = state
        answerScope().launch {
            snapshotFlow { state.value }
                .debounce(ANSWER_SAVE_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collect { value ->
                    settings.putString(settingsKey, value)
                }
        }
        return state
    }

    fun restoreFeedback(questionId: String): Feedback? {
        val type = settings.getString(feedbackKey(questionId, FEEDBACK_TYPE_KEY), "")
        if (type.isBlank()) return null
        val answer = settings.getString(feedbackKey(questionId, FEEDBACK_ANSWER_KEY), "")
        val title = settings.getString(feedbackKey(questionId, FEEDBACK_TITLE_KEY), "")
        val text = settings.getString(feedbackKey(questionId, FEEDBACK_TEXT_KEY), "")
        return when (type) {
            FEEDBACK_TYPE_CORRECT -> Feedback.Correct(answer, title, text)
            FEEDBACK_TYPE_INCORRECT -> Feedback.Incorrect(answer, title, text)
            else -> null
        }
    }

    fun saveFeedback(questionId: String, feedback: Feedback) {
        val type = when (feedback) {
            is Feedback.Correct -> FEEDBACK_TYPE_CORRECT
            is Feedback.Incorrect -> FEEDBACK_TYPE_INCORRECT
            is Feedback.Loading -> return
        }
        settings.putString(feedbackKey(questionId, FEEDBACK_TYPE_KEY), type)
        settings.putString(feedbackKey(questionId, FEEDBACK_ANSWER_KEY), feedback.answer)
        settings.putString(feedbackKey(questionId, FEEDBACK_TITLE_KEY), feedback.title)
        settings.putString(feedbackKey(questionId, FEEDBACK_TEXT_KEY), feedback.text)
    }

    fun restoreLastQuestionIndex(totalQuestions: Int): Int {
        if (totalQuestions <= 0) return 0
        val saved = settings.getInt(lastQuestionKey(), 0)
        return saved.coerceIn(0, totalQuestions - 1)
    }

    fun saveLastQuestionIndex(index: Int) {
        settings.putInt(lastQuestionKey(), index)
    }

    private fun answerKey(questionId: String) = "$ANSWER_PREFIX.$lessonId.$questionId"
    private fun feedbackKey(questionId: String, field: String) = "$FEEDBACK_PREFIX.$lessonId.$questionId.$field"
    private fun lastQuestionKey() = "$LAST_QUESTION_PREFIX.$lessonId"

    private fun answerScope(): CoroutineScope {
        val existing = answerScope
        if (existing != null && answerScopeJob?.isActive == true) return existing
        val parentJob = scope.coroutineContext[Job]
        val job = SupervisorJob(parentJob)
        val newScope = CoroutineScope(scope.coroutineContext + job)
        answerScopeJob = job
        answerScope = newScope
        return newScope
    }

    private companion object {
        private const val ANSWER_PREFIX = "open_question.answer"
        private const val FEEDBACK_PREFIX = "open_question.feedback"
        private const val LAST_QUESTION_PREFIX = "open_question.last_index"
        private const val FEEDBACK_TYPE_KEY = "type"
        private const val FEEDBACK_TITLE_KEY = "title"
        private const val FEEDBACK_TEXT_KEY = "text"
        private const val FEEDBACK_ANSWER_KEY = "answer"
        private const val FEEDBACK_TYPE_CORRECT = "correct"
        private const val FEEDBACK_TYPE_INCORRECT = "incorrect"
        private const val ANSWER_SAVE_DEBOUNCE_MS = 500L
    }

}
