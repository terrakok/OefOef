package com.github.terrakok.oefoef

import com.russhwolf.settings.Settings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json

@SingleIn(AppScope::class)
@Inject
class LessonStatsService(
    private val json: Json,
    private val settings: Settings
) {

    // lessonId -> last open question index
    private val lastOpenQuestionMap: MutableMap<String, Int> by lazy {
        val serialized = settings.getString(LAST_OPEN_QUESTION_KEY, "{}")
        json.decodeFromString<Map<String, Int>>(serialized).toMutableMap()
    }

    fun getLastOpenQuestionIndex(lessonId: String): Int =
        lastOpenQuestionMap[lessonId] ?: 0

    fun setLastOpenQuestionIndex(lessonId: String, questionIx: Int) {
        lastOpenQuestionMap[lessonId] = questionIx
        // TODO: avoid full serialization on every call if it ever becomes a problem. KISS for now
        settings.putString(LAST_OPEN_QUESTION_KEY, json.encodeToString(lastOpenQuestionMap))
    }

    companion object {
        private const val LAST_OPEN_QUESTION_KEY = "com.github.terrakok.oefoef.lesson_last_open_q"
    }
}