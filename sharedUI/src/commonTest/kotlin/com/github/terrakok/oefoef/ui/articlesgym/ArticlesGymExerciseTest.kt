package com.github.terrakok.oefoef.ui.articlesgym

import com.github.terrakok.oefoef.entity.ArticleCheckState
import com.github.terrakok.oefoef.entity.ArticleChoice
import com.github.terrakok.oefoef.entity.ArticlesGymExercise
import com.github.terrakok.oefoef.entity.checkAnswers
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
}
