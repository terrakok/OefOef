package com.github.terrakok.oefoef.ui.articlesgym

import com.github.terrakok.oefoef.domain.ArticlesGymRepository
import com.github.terrakok.oefoef.entity.ArticleCheckState
import com.github.terrakok.oefoef.entity.ArticleChoice
import com.github.terrakok.oefoef.entity.ArticlesGymExercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesGymViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val exercise1 = ArticlesGymExercise(
        verbatimSentence = "Zij zijn het niet eens met alle besluiten van Geert Wilders, de leider van de partij.",
        sentenceWithPlaceholders = "Zij zijn het niet eens met alle (?1) besluiten van (?2) Geert Wilders, (?3) leider van (?4) partij.",
        placeholderCount = 4,
        explanations = emptyList()
    )

    private class FakeRepository(val exercises: List<ArticlesGymExercise>) : ArticlesGymRepository(Json, Dispatchers.Unconfined) {
        override suspend fun getExercises(): List<ArticlesGymExercise> = exercises
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test calculateCorrectChoices for exercise 1`() = runTest {
        val vm = ArticlesGymViewModel(0, FakeRepository(listOf(exercise1)))
        advanceUntilIdle()

        // We need to access private calculateCorrectChoices via a public way or just test the side effect
        // checkAnswers uses correctChoices
        
        // Fill correct answers
        // 1: Zero, 2: Zero, 3: De, 4: De
        vm.onAnswerUpdated(0, ArticleChoice.Zero)
        vm.onAnswerUpdated(1, ArticleChoice.Zero)
        vm.onAnswerUpdated(2, ArticleChoice.De)
        vm.onAnswerUpdated(3, ArticleChoice.De)
        
        vm.checkAnswers()
        
        assertEquals(ArticleCheckState.Correct, vm.answers[0]?.state, "Answer 1 should be Correct")
        assertEquals(ArticleCheckState.Correct, vm.answers[1]?.state, "Answer 2 should be Correct")
        assertEquals(ArticleCheckState.Correct, vm.answers[2]?.state, "Answer 3 should be Correct")
        assertEquals(ArticleCheckState.Correct, vm.answers[3]?.state, "Answer 4 should be Correct")
    }

    @Test
    fun `test calculateCorrectChoices for exercise 2`() = runTest {
        val exercise2 = ArticlesGymExercise(
            verbatimSentence = "De Partij voor de Vrijheid is een grote partij in Nederland.",
            sentenceWithPlaceholders = "(?1) Partij voor (?2) Vrijheid is (?3) grote partij in (?4) Nederland.",
            placeholderCount = 4,
            explanations = emptyList()
        )
        val vm = ArticlesGymViewModel(0, FakeRepository(listOf(exercise2)))
        advanceUntilIdle()

        // 1: De, 2: de, 3: een, 4: Zero
        vm.onAnswerUpdated(0, ArticleChoice.De)
        vm.onAnswerUpdated(1, ArticleChoice.De)
        vm.onAnswerUpdated(2, ArticleChoice.Een)
        vm.onAnswerUpdated(3, ArticleChoice.Zero)
        
        vm.checkAnswers()
        
        assertEquals(ArticleCheckState.Correct, vm.answers[0]?.state, "Answer 1 should be Correct")
        assertEquals(ArticleCheckState.Correct, vm.answers[1]?.state, "Answer 2 should be Correct")
        assertEquals(ArticleCheckState.Correct, vm.answers[2]?.state, "Answer 3 should be Correct")
        assertEquals(ArticleCheckState.Correct, vm.answers[3]?.state, "Answer 4 should be Correct")
    }
}
