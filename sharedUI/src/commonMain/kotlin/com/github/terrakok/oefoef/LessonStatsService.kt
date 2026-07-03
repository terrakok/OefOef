package com.github.terrakok.oefoef

import com.russhwolf.settings.Settings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@Inject
class LessonStatsService(
    private val settings: Settings
) {
    fun getLastOpenQuestionIndex(lessonId: String): Int =
        settings.getInt(lastOpenQuestionKey(lessonId), 0)

    fun setLastOpenQuestionIndex(lessonId: String, questionIx: Int) {
        settings.putInt(lastOpenQuestionKey(lessonId), questionIx)
    }

    companion object {
        private fun lastOpenQuestionKey(lessonId: String) = "com.github.terrakok.oefoef.lesson_last_open_q_${lessonId}"
    }
}