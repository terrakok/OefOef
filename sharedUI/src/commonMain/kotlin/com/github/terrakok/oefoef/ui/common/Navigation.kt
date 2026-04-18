package com.github.terrakok.oefoef.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

internal sealed interface AppNavKey : NavKey

internal data object WelcomeScreen : AppNavKey

internal data object GymScreen : AppNavKey

internal data class LessonScreen(
    val id: String,
) : AppNavKey

internal data class OpenQuestionScreen(
    val id: String,
) : AppNavKey

internal data class ArticlesGymScreen(
    val exerciseIndex: Int = 0,
) : AppNavKey

@Composable
internal expect fun BrowserNavigation(backStack: SnapshotStateList<AppNavKey>)
