package com.github.terrakok.oefoef.entity

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class ArticlesGymExercise(
    val verbatimSentence: String,
    val sentenceWithPlaceholders: String,
    val placeholderCount: Int,
    val explanations: List<String>,
)

@JvmInline
value class ArticleChoice private constructor(val value: String) {
    companion object {
        val Zero = ArticleChoice("-")
        val De = ArticleChoice("de")
        val Het = ArticleChoice("het")
        val Een = ArticleChoice("een")

        val values = listOf(Zero, De, Het, Een)
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

fun String.parseExplanation(): ParsedExplanation? {
    val match = Regex("^`(.+?)`\\s*-\\s*(.+)$").find(this)
    return match?.let {
        ParsedExplanation(
            answer = it.groupValues[1].trim(),
            explanation = it.groupValues[2].trim(),
        )
    }
}

fun ArticlesGymExercise.calculateCorrectChoices(): List<ArticleChoice> {
    val choices = mutableListOf<ArticleChoice>()

    fun escapeForRegex(s: String): String {
        val specialChars = "\\^$.|?*+()[]{}"
        return s.map { c ->
            if (specialChars.contains(c)) "\\$c" else c.toString()
        }.joinToString("").replace(" ", "\\s*")
    }

    var regexPattern = ""
    val placeholderRegex = Regex("\\(\\?(\\d+)\\)")
    var lastIndex = 0

    val matches = placeholderRegex.findAll(sentenceWithPlaceholders).toList()
    for (match in matches) {
        val staticPart = sentenceWithPlaceholders.substring(lastIndex, match.range.first)
        regexPattern += escapeForRegex(staticPart)
        regexPattern += "(.*?)"
        lastIndex = match.range.last + 1
    }
    val finalStaticPart = sentenceWithPlaceholders.substring(lastIndex)
    regexPattern += escapeForRegex(finalStaticPart)

    val regex = Regex("^$regexPattern$", RegexOption.IGNORE_CASE)
    val match = regex.find(verbatimSentence.trim())

    if (match != null) {
        for (i in 1..placeholderCount) {
            val valInSentence = match.groupValues[i].trim().lowercase()
            val choice = when (valInSentence) {
                "de" -> ArticleChoice.De
                "het" -> ArticleChoice.Het
                "een" -> ArticleChoice.Een
                else -> ArticleChoice.Zero
            }
            choices.add(choice)
        }
    }
    return choices
}

fun ArticlesGymExercise.checkAnswers(userChoices: List<ArticleChoice?>): List<ArticleCheckState> {
    val correctOnes = calculateCorrectChoices()
    return userChoices.mapIndexed { index, choice ->
        when {
            choice == null -> ArticleCheckState.Pending
            choice == correctOnes.getOrNull(index) -> ArticleCheckState.Correct
            else -> ArticleCheckState.Incorrect
        }
    }
}
