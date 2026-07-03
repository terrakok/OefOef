package com.github.terrakok.oefoef.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.terrakok.navigation3.browser.HierarchicalBrowserNavigation

@Composable
internal actual fun BrowserNavigation(backStack: SnapshotStateList<AppNavKey>) {
    HierarchicalBrowserNavigation {
        when (val key = backStack.lastOrNull() as AppNavKey) {
            is WelcomeScreen -> ""
            is LessonScreen -> "#/lesson/${key.id}"
            is OpenQuestionScreen -> "#/lesson/${key.id}"
            is ArticlesGymScreen -> "#/articles-gym"
            is GymScreen -> "#/gym"
        }
    }
}
