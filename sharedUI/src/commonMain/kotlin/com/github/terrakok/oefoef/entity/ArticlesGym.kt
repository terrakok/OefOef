package com.github.terrakok.oefoef.entity

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class ArticlesGymExercise(
    val verbatimSentence: String,
    val sentenceWithPlaceholders: String,
    val placeholderCount: Int,
    val explanations: List<String>,
) {
    private val correctChoices: List<ArticleChoice> by lazy {
        val regexPattern = PLACEHOLDER_REGEX.split(sentenceWithPlaceholders)
            .joinToString("(.*?)") { it.escapeForRegex() }

        val regex = Regex("^$regexPattern$", RegexOption.IGNORE_CASE)
        val match = regex.find(verbatimSentence.trim())

        (1..placeholderCount).map { i ->
            ArticleChoice.fromValue(match?.groupValues?.getOrNull(i) ?: "")
        }
    }

    val parsedExplanations: List<ParsedExplanation> by lazy {
        explanations.mapNotNull { it.parseExplanation() }
    }

    fun checkAnswers(userChoices: List<ArticleChoice?>): List<ArticleCheckState> =
        userChoices.mapIndexed { index, choice ->
            when {
                choice == null -> ArticleCheckState.Pending
                choice == correctChoices.getOrNull(index) -> ArticleCheckState.Correct
                else -> ArticleCheckState.Incorrect
            }
        }

    private fun String.parseExplanation(): ParsedExplanation? =
        EXPLANATION_REGEX.find(this)?.let { match ->
            val (answer, explanation) = match.destructured
            ParsedExplanation(answer.trim(), explanation.trim())
        }

    private fun String.escapeForRegex(): String =
        this.map { if ("\\^$.|?*+()[]{}".contains(it)) "\\$it" else it.toString() }
            .joinToString("")
            .replace(" ", "\\s*")

    companion object {
        private val EXPLANATION_REGEX = Regex("^`(.+?)`\\s*-\\s*(.+)$")
        private val PLACEHOLDER_REGEX = Regex("\\(\\?\\d+\\)")
    }
}

@JvmInline
value class ArticleChoice private constructor(val value: String) {
    companion object {
        val Zero = ArticleChoice("-")
        val De = ArticleChoice("de")
        val Het = ArticleChoice("het")
        val Een = ArticleChoice("een")

        val values = listOf(Zero, De, Het, Een)

        fun fromValue(value: String) =
            values.find { it.value == value.trim().lowercase() } ?: Zero
    }
}

enum class ArticleCheckState {
    Pending, Correct, Incorrect
}

data class ArticleAnswer(
    val choice: ArticleChoice?,
    val state: ArticleCheckState = ArticleCheckState.Pending,
)

data class ParsedExplanation(
    val answer: String,
    val explanation: String,
)
