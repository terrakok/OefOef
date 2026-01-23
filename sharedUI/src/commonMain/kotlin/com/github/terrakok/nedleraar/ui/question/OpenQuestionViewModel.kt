package com.github.terrakok.nedleraar.ui.question

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.nedleraar.DataService
import com.github.terrakok.nedleraar.Lesson
import com.russhwolf.settings.Settings
import dev.zacsweers.metro.*
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.launch

@Immutable
sealed interface Feedback {
    val answer: String
    val title: String
    val text: String
    data class Loading(
        override val answer: String,
        override val title: String = "",
        override val text: String = ""
    ) : Feedback
    data class Correct(
        override val answer: String,
        override val title: String,
        override val text: String
    ) : Feedback
    data class Incorrect(
        override val answer: String,
        override val title: String,
        override val text: String
    ) : Feedback
}

@AssistedInject
class OpenQuestionViewModel(
    @Assisted val lessonId: String,
    val dataService: DataService,
    settings: Settings
) : ViewModel() {
    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(lessonId: String): OpenQuestionViewModel
    }

    var lesson by mutableStateOf<Lesson?>(null)
        private set

    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    var currentQuestionIndex by mutableStateOf(0)
        private set

    private val answers = mutableStateMapOf<String, MutableState<String>>()
    private val results = mutableStateMapOf<String, Feedback>()
    private val stateStore = OpenQuestionStateStore(lessonId, settings, viewModelScope)

    val question get() = lesson?.let { it.questions[currentQuestionIndex] } ?: error("Lesson is null")
    val answer get() = answers.getOrPut(question.id) { stateStore.createAnswerState(question.id) }
    val feedback get() = results[question.id]

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                val loadedLesson = dataService.getLesson(lessonId)
                restoreLessonState(loadedLesson)
                lesson = loadedLesson
            } catch (e: Throwable) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun getFeedback() {
        checkAnswer(question.id, answer.value)
    }

    private fun checkAnswer(qId: String, answer: String) {
        val loadedLessonId = lesson?.id ?: return
        viewModelScope.launch {
            if (results[qId] is Feedback.Loading) return@launch
            setFeedback(qId, Feedback.Loading(answer))
            val result = dataService.checkAnswer(loadedLessonId, qId, answer)

            val feedback = if (!result.contains("Score:")) {
                Feedback.Incorrect(answer, "Try again", result)
            } else {
                val regex = Regex("""Score:\s*([0-9]+(?:\.[0-9]+)?)\s*/\s*([0-9]+(?:\.[0-9]+)?)""")
                val match = regex.find(result)
                val score = match?.destructured?.component1()?.toFloatOrNull() ?: 0f

                if (score >= 3f) {
                    Feedback.Correct(answer, "Good", result)
                } else {
                    Feedback.Incorrect(answer, "Too many mistakes", result)
                }
            }
            setFeedback(qId, feedback)
        }
    }

    fun nextQuestion() {
        setQuestionIndex(currentQuestionIndex + 1)
    }

    fun previousQuestion() {
        setQuestionIndex(currentQuestionIndex - 1)
    }

    private fun restoreLessonState(lesson: Lesson) {
        stateStore.clear()
        answers.clear()
        results.clear()
        lesson.questions.forEach { question ->
            stateStore.restoreFeedback(question.id)?.let { results[question.id] = it }
        }
        currentQuestionIndex = stateStore.restoreLastQuestionIndex(lesson.questions.size)
    }

    private fun setFeedback(questionId: String, feedback: Feedback) {
        results[questionId] = feedback
        if (feedback !is Feedback.Loading) {
            stateStore.saveFeedback(questionId, feedback)
        }
    }

    private fun setQuestionIndex(index: Int) {
        val max = lesson?.questions?.size ?: 0
        val clamped = if (max == 0) 0 else index.coerceIn(0, max - 1)
        if (clamped == currentQuestionIndex) return
        currentQuestionIndex = clamped
        stateStore.saveLastQuestionIndex(clamped)
    }
}
