package com.github.terrakok.oefoef.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

@Immutable
class YouTubeController {
    private var playerStateGetter: (() -> YTPlayerState?)? = null
    private fun getYtPlayerState(): YTPlayerState? = playerStateGetter?.invoke()

    var play by mutableStateOf(false)
    val progress = Channel<Int>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val seek = Channel<Int>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val setPauseResumeRequests = Channel<SetPauseResume>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun onProgress(progress: Int) {
        this.progress.trySend(progress)
    }

    fun seekTo(seek: Int) {
        play = true
        this.seek.trySend(seek)
        this.progress.trySend(seek)
    }

    fun setPauseOrResume(state: SetPauseResume) {
        setPauseResumeRequests.trySend(state)
    }

    fun setYTPlayerStateGetter(playerStateGetter: (() -> YTPlayerState?)?) {
        this.playerStateGetter = playerStateGetter
    }

    fun isPlaying(): Boolean = getYtPlayerState() == YTPlayerState.PLAYING

    enum class SetPauseResume {
        PAUSE, RESUME
    }

    enum class YTPlayerState(val value: Int) {
        PLAYING(1),
        PAUSED(2),
        ENDED(0),
        UNSTARTED(-1),
        BUFFERING(3)
    }
}

@Composable
expect fun YouTubeWidget(
    videoId: String,
    controller: YouTubeController,
    modifier: Modifier = Modifier
)