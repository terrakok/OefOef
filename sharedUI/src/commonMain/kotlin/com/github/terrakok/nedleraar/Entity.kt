package com.github.terrakok.nedleraar

import androidx.compose.runtime.Immutable

data class LessonHeader(
    val id: String,
    val title: String,
    val previewUrl: String,
    val lengthSeconds: Int
)

@Immutable
data class Lesson(
    val id: String,
    val videoId: String,
    val title: String,
    val previewUrl: String,
    val lengthSeconds: Int,
    val videoTranscription: List<TranscriptionItem>,
    val questions: List<OpenQuestion>
)

data class TranscriptionItem(
    val timestamp: Int,
    val text: String
)

data class OpenQuestion(
    val id: String,
    val text: String
)