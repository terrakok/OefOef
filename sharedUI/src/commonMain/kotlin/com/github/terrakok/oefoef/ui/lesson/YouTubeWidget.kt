package com.github.terrakok.oefoef.ui.lesson

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

@Immutable
class YouTubeController {
    var play by mutableStateOf(false)
    val progress = Channel<Int>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val seek = Channel<Int>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val pauses = Channel<Boolean>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun onProgress(progress: Int) {
        this.progress.trySend(progress)
    }

    fun seekTo(seek: Int) {
        play = true
        this.seek.trySend(seek)
        this.progress.trySend(seek)
    }

    fun setOnPause(pause: Boolean) {
        pauses.trySend(pause)
    }
}

@Composable
expect fun YouTubeWidget(
    videoId: String,
    controller: YouTubeController,
    modifier: Modifier = Modifier
)