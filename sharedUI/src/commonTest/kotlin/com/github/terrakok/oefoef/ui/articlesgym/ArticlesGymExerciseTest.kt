package com.github.terrakok.oefoef.ui.articlesgym

import com.github.terrakok.oefoef.entity.ArticleCheckState
import com.github.terrakok.oefoef.entity.ArticleChoice
import com.github.terrakok.oefoef.entity.ArticlesGymExercise
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesGymExerciseTest {

    @Test
    fun testCalculateCorrectChoices() = runTest {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "Zij zijn het niet eens met alle besluiten van Geert Wilders, de leider van de partij.",
            sentenceWithPlaceholders = "Zij zijn het niet eens met alle (?1) besluiten van (?2) Geert Wilders, (?3) leider van (?4) partij.",
            placeholderCount = 4,
            explanations = emptyList()
        )

        val res = exercise.checkAnswers(
            listOf(ArticleChoice.Zero, ArticleChoice.Zero, ArticleChoice.De, ArticleChoice.De)
        )

        res.forEach { assertEquals(ArticleCheckState.Correct, it) }
    }

    @Test
    fun testCalculateCorrectChoices2() = runTest {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "De Partij voor de Vrijheid is een grote partij in Nederland.",
            sentenceWithPlaceholders = "(?1) Partij voor (?2) Vrijheid is (?3) grote partij in (?4) Nederland.",
            placeholderCount = 4,
            explanations = emptyList()
        )

        val res = exercise.checkAnswers(
            listOf(ArticleChoice.De, ArticleChoice.De, ArticleChoice.Een, ArticleChoice.Zero)
        )

        res.forEach { assertEquals(ArticleCheckState.Correct, it) }
    }

    @Test
    fun testMixedChoices() = runTest {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "De auto staat in de garage.",
            sentenceWithPlaceholders = "(?1) auto staat in (?2) garage.",
            placeholderCount = 2,
            explanations = emptyList()
        )

        val res = exercise.checkAnswers(
            listOf(ArticleChoice.De, ArticleChoice.Het)
        )

        assertEquals(ArticleCheckState.Correct, res[0])
        assertEquals(ArticleCheckState.Incorrect, res[1])
    }

    @Test
    fun testPendingChoices() = runTest {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "Het boek ligt op de tafel.",
            sentenceWithPlaceholders = "(?1) boek ligt op (?2) tafel.",
            placeholderCount = 2,
            explanations = emptyList()
        )

        val res = exercise.checkAnswers(
            listOf(ArticleChoice.Het, null)
        )

        assertEquals(ArticleCheckState.Correct, res[0])
        assertEquals(ArticleCheckState.Pending, res[1])
    }

    @Test
    fun testHetAndEen() = runTest {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "Het is een mooie dag.",
            sentenceWithPlaceholders = "(?1) is (?2) mooie dag.",
            placeholderCount = 2,
            explanations = emptyList()
        )

        val res = exercise.checkAnswers(
            listOf(ArticleChoice.Het, ArticleChoice.Een)
        )

        res.forEach { assertEquals(ArticleCheckState.Correct, it) }
    }

    @Test
    fun testZeroArticles() = runTest {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "Water is gezond.",
            sentenceWithPlaceholders = "(?1) Water is (?2) gezond.",
            placeholderCount = 2,
            explanations = emptyList()
        )

        val res = exercise.checkAnswers(
            listOf(ArticleChoice.Zero, ArticleChoice.Zero)
        )

        res.forEach { assertEquals(ArticleCheckState.Correct, it) }
    }

    @Test
    fun testAllIncorrect() = runTest {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "De man loopt naar de winkel.",
            sentenceWithPlaceholders = "(?1) man loopt naar (?2) winkel.",
            placeholderCount = 2,
            explanations = emptyList()
        )

        val res = exercise.checkAnswers(
            listOf(ArticleChoice.Het, ArticleChoice.Een)
        )

        res.forEach { assertEquals(ArticleCheckState.Incorrect, it) }
    }

    @Test
    fun testParseExplanation() {
        val exercise = ArticlesGymExercise(
            verbatimSentence = "De man loopt naar de winkel.",
            sentenceWithPlaceholders = "(?1) man loopt naar (?2) winkel.",
            placeholderCount = 2,
            explanations = listOf(
                "`de` - used for masculine and feminine nouns",
                "`de` - used for masculine and feminine nouns"
            )
        )

        assertEquals("de", exercise.parsedExplanations[0].answer)
        assertEquals("used for masculine and feminine nouns", exercise.parsedExplanations[0].explanation)
        assertEquals("de", exercise.parsedExplanations[1].answer)
        assertEquals("used for masculine and feminine nouns", exercise.parsedExplanations[1].explanation)
    }
}
