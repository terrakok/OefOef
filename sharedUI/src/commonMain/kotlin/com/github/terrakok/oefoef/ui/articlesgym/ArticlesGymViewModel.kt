package com.github.terrakok.oefoef.ui.articlesgym

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.oefoef.domain.DataRepository
import com.github.terrakok.oefoef.entity.ArticleAnswer
import com.github.terrakok.oefoef.entity.ArticleCheckState
import com.github.terrakok.oefoef.entity.ArticleChoice
import com.github.terrakok.oefoef.entity.ArticlesGymExercise
import com.github.terrakok.oefoef.entity.calculateCorrectChoices
import dev.zacsweers.metro.*
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.launch

@AssistedInject
class ArticlesGymViewModel(
    @Assisted val exerciseIndex: Int,
    val dataRepository: DataRepository,
) : ViewModel() {
    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(exerciseIndex: Int): ArticlesGymViewModel
    }

    var exercises by mutableStateOf<List<ArticlesGymExercise>?>(null)
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var currentExerciseIndex by mutableIntStateOf(exerciseIndex)
        private set

    private val _answers = mutableStateListOf<ArticleAnswer?>()
    val answers: List<ArticleAnswer?> get() = _answers

    private var correctChoices = listOf<ArticleChoice>()

    val currentExercise: ArticlesGymExercise? get() = exercises?.getOrNull(currentExerciseIndex)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                exercises = dataRepository.getArticlesTrainingExercises()
                syncAnswersCount()
            } catch (e: Throwable) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun isAllCorrect(): Boolean {
        if (correctChoices.size != currentExercise?.placeholderCount) return false
        return _answers.indices.all { idx ->
            _answers[idx]?.choice == correctChoices.getOrNull(idx)
        }
    }

    fun onAnswerUpdated(placeholderIndex: Int, answer: ArticleChoice) {
        val current = _answers[placeholderIndex]
        _answers[placeholderIndex] = if (current != null) {
            current.copy(choice = answer, state = ArticleCheckState.Pending)
        } else {
            ArticleAnswer(answer, ArticleCheckState.Pending)
        }
        checkIfAllFilledAndVerify()
    }

    fun checkAnswers() {
        val exercise = currentExercise ?: return
        for (idx in 0 until exercise.placeholderCount) {
            val current = _answers[idx]
            val isCorrect = current?.choice == correctChoices.getOrNull(idx)
            _answers[idx] = current?.copy(
                state = if (isCorrect) ArticleCheckState.Correct else ArticleCheckState.Incorrect
            )
        }
    }

    fun nextExercise() {
        _answers.clear()
        val max = exercises?.size ?: 0
        currentExerciseIndex = (currentExerciseIndex + 1).coerceIn(0..<max)
        syncAnswersCount()
    }

    fun previousExercise() {
        _answers.clear()
        val max = exercises?.size ?: 0
        currentExerciseIndex = (currentExerciseIndex - 1).coerceIn(0..<max)
        syncAnswersCount()
    }

    private fun syncAnswersCount() {
        val exercise = currentExercise ?: return
        while (_answers.size < exercise.placeholderCount) {
            _answers.add(ArticleAnswer(null, ArticleCheckState.Pending))
        }
        while (_answers.size > exercise.placeholderCount) {
            _answers.removeAt(_answers.size - 1)
        }
        correctChoices = exercise.calculateCorrectChoices()
    }

    private fun checkIfAllFilledAndVerify() {
        val allFilled = _answers.isNotEmpty() && _answers.all { it != null && it.choice != null }
        if (allFilled) {
            checkAnswers()
        }
    }
}
