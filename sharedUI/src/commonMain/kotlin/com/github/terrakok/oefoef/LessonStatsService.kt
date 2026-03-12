package com.github.terrakok.oefoef

import com.russhwolf.settings.Settings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
@SingleIn(AppScope::class)
@Inject
class LessonStatsService(
    private val settings: Settings,
    private val json: Json,
    private val appCoroutineScope: CoroutineScope
) {

    companion object {
        private const val LESSON_STATS_STATE_KEY = "com.github.terrakok.oefoef.saved_lesson_stats_key"
    }

    private val saveLessonStatsFlow = MutableSharedFlow<Map<String, LessonStats>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val lessonStatsData = mutableMapOf<String, LessonStats>().also { initState ->
        val saved = settings.getStringOrNull(LESSON_STATS_STATE_KEY)?.let { str ->
            json.decodeFromString<Map<String, LessonStats>>(str)
        }
        saved?.let { initState.putAll(it) }
    }

    init {
        appCoroutineScope.launch {
            saveLessonStatsFlow.debounce(1.seconds).collect {
                Log.d("Saving lessons stats")
                settings.putString(LESSON_STATS_STATE_KEY, json.encodeToString(it))
            }
        }
    }

    fun getLastOpenQuestionIndex(lessonId: String): Int =
        lessonStatsData[lessonId]?.lastOpenQuestionIx ?: 0

    fun setLastOpenQuestionIndex(lessonId: String, questionIx: Int) {
        val currentStats = lessonStatsData.getOrPut(lessonId) { LessonStats() }
        lessonStatsData[lessonId] = currentStats.copy(lastOpenQuestionIx = questionIx)
        saveLessonStatsFlow.tryEmit(lessonStatsData)
    }

}

@Serializable
data class LessonStats(
    val lastOpenQuestionIx: Int = 0
)