package com.github.terrakok.oefoef

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.Instant

data class ApiConfiguration(
    val apiUrl: String,
    val collections: List<CollectionApi> = emptyList(),
    val activeLessonsCollectionId: String = "nl_en"
) {

    data class CollectionApi(
        val id: String,
        val indexRootUrl: String,
    )

    companion object {
        val LEGACY = ApiConfiguration(
            apiUrl = "https://eymar.nl/lang-practice/api",
            collections = listOf(
                CollectionApi(
                    id = "nl_en",
                    indexRootUrl = "https://eymar.nl/lang-practice/datav2"
                )
            )
        )

        val OEF_OEF = ApiConfiguration(
            apiUrl = "https://api.oefoef.app/",
            collections = listOf(
                CollectionApi(
                    id = "nl_en",
                    indexRootUrl = "https://collections.oefoef.app/nl_en",
                )
            ),
            activeLessonsCollectionId = "nl_en"
        )
    }
}

@Inject
@SingleIn(AppScope::class)
class DataService(
    private val httpClient: HttpClient,
    private val apiConfiguration: ApiConfiguration = ApiConfiguration.OEF_OEF,
) {

    private val dispatcher = Dispatchers.Default.limitedParallelism(1)

    private val headers = mutableListOf<LessonHeader>()
    private val lessons = mutableMapOf<String, Lesson>()

    private val activeCollection = apiConfiguration.collections.find {
        it.id == apiConfiguration.activeLessonsCollectionId
    } ?: error("No active collection found with id=${apiConfiguration.activeLessonsCollectionId}")

    suspend fun getLessons(forceRefresh: Boolean = false): List<LessonHeader> = withContext(dispatcher) {
        if (headers.isEmpty() || forceRefresh) {
            val json = httpClient.get(activeCollection.indexRootUrl + "/index.json").body<JsonArray>()
            val new = json.map {
                val jo = it.jsonObject
                LessonHeader(
                    id = jo.getValue("id").jsonPrimitive.content,
                    title = jo.getValue("title").jsonPrimitive.content,
                    previewUrl = jo.getValue("pictureUrl").jsonPrimitive.content,
                    lengthSeconds = jo["videoDuration"]?.jsonPrimitive?.int ?: 0,
                    createdAt = Instant.parse(jo.getValue("createdAt").jsonPrimitive.content.replace(" ", "T"))
                )
            }.sortedByDescending { it.createdAt }
            headers.addAll(new)
        }
        headers
    }

    suspend fun getLesson(id: String): Lesson = withContext(dispatcher) {
        lessons.getOrPut(id) {
            val jo = httpClient.get(activeCollection.indexRootUrl + "/items/${id}.json").body<JsonObject>()
            val transcription = jo.getValue("transcriptionSentences").jsonArray.map { item ->
                val jo = item.jsonObject
                val text = jo.getValue("text").jsonPrimitive.content
                val translation = if (jo.containsKey("translation")) {
                    jo.getValue("translation").jsonPrimitive.content
                } else {
                    text
                }
                TranscriptionItem(
                    time = jo.getValue("start").jsonPrimitive.float.toInt(),
                    text = text,
                    translationEn = translation
                )
            }
            val practice = jo.getValue("practice").jsonObject
            val questions = practice.getValue("open_questions").jsonArray.mapIndexed { index, element ->
                val q = element.jsonObject
                OpenQuestion(
                    id = q.getValue("id").jsonPrimitive.content,
                    text = q.getValue("question").jsonPrimitive.content,
                    textEn = q.getValue("question_en").jsonPrimitive.content
                )
            }
            Lesson(
                id = id,
                videoId = jo.getValue("videoId").jsonPrimitive.content,
                title = jo.getValue("title").jsonPrimitive.content,
                previewUrl = jo.getValue("pictureUrl").jsonPrimitive.content,
                lengthSeconds = jo.getValue("videoDuration").jsonPrimitive.int,
                videoTranscription = transcription,
                questions = questions,
                createdAt = Instant.parse(jo.getValue("createdAt").jsonPrimitive.content.replace(" ", "T")),
                lang = jo.getValue("language").jsonPrimitive.content
            )
        }
    }

    suspend fun checkAnswer(
        lessonId: String,
        questionId: String,
        answer: String
    ): CheckAnswerResponse {
        val lang = getLesson(lessonId).lang
        val jo = httpClient.get(apiConfiguration.apiUrl + "/check_answer", {
            parameter("lessonId", lessonId)
            parameter("lang", lang)
            parameter("questionId", questionId)
            parameter("answer", answer)
            timeout {
                socketTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
            }
        }).body<JsonObject>()

        if (jo.containsKey("error") && jo["error"] != null) {
            return CheckAnswerResponse(error = jo.getValue("error").jsonPrimitive.content)
        }

        return CheckAnswerResponse(result = jo.getValue("result").jsonPrimitive.content)
    }

    data class CheckAnswerResponse(
        val result: String? = null,
        val error: String? = null
    )
}