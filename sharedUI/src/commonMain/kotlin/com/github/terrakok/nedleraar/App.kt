package com.github.terrakok.nedleraar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.github.terrakok.nedleraar.ui.AppTheme
import com.github.terrakok.nedleraar.ui.SplitSceneStrategy
import com.github.terrakok.nedleraar.ui.lesson.LessonPage
import com.github.terrakok.nedleraar.ui.question.OpenQuestionPage
import com.github.terrakok.nedleraar.ui.rememberSplitSceneStrategy
import com.github.terrakok.nedleraar.ui.welcome.WelcomePage

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = WithAppGraph {
    AppTheme(onThemeChanged) {
        val backStack = remember { mutableStateListOf<NavKey>(WelcomeScreen) }

        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerLowest),
            sceneStrategy = rememberSplitSceneStrategy(),
            entryProvider = entryProvider {
                entry<WelcomeScreen> {
                    WelcomePage(
                        onLessonHeaderClick = { lesson ->
                            backStack.add(LessonScreen(lesson.id))
                            backStack.add(OpenQuestionScreen)
                        }
                    )
                }
                entry<LessonScreen>(
                    metadata = SplitSceneStrategy.split()
                ) {
                    LessonPage(
                        id = it.videoId,
                        onBackClick = { if (backStack.size > 1) backStack.removeLast() }
                    )
                }
                entry<OpenQuestionScreen> {
                    OpenQuestionPage("Wat is het verschil tussen de vrije sector en de sociale sector voor huren?", 12, 5)
                }
            }
        )
    }
}

private data object WelcomeScreen : NavKey
private data class LessonScreen(val videoId: String) : NavKey
private data object OpenQuestionScreen : NavKey