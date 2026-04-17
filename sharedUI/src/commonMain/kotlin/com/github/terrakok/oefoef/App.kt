package com.github.terrakok.oefoef

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.github.terrakok.oefoef.domain.ClientSpellcheck
import com.github.terrakok.oefoef.domain.DisabledClientSpellCheck
import com.github.terrakok.oefoef.ui.common.AppNavKey
import com.github.terrakok.oefoef.ui.common.AppTheme
import com.github.terrakok.oefoef.ui.common.BrowserNavigation
import com.github.terrakok.oefoef.ui.common.LessonScreen
import com.github.terrakok.oefoef.ui.common.OpenQuestionScreen
import com.github.terrakok.oefoef.ui.common.SplitSceneStrategy
import com.github.terrakok.oefoef.ui.common.WelcomeScreen
import com.github.terrakok.oefoef.ui.common.rememberSplitSceneStrategy
import com.github.terrakok.oefoef.ui.lesson.LessonPage
import com.github.terrakok.oefoef.ui.question.OpenQuestionPage
import com.github.terrakok.oefoef.ui.welcome.WelcomePage
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

@Preview
@Composable
fun App(
    clientSpellcheck: ClientSpellcheck = DisabledClientSpellCheck(),
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {},
) = WithAppGraph(clientSpellcheck) {
    AppTheme(onThemeChanged) {
        val backStack = remember { mutableStateListOf<AppNavKey>(WelcomeScreen) }
        BrowserNavigation(backStack)

        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val isWide = windowSizeClass.isWide()
        val last = backStack.lastOrNull()
        LaunchedEffect(isWide, last) {
            if (isWide && last is LessonScreen) {
                backStack.add(OpenQuestionScreen(last.id))
            }
        }

        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            sceneStrategies = listOf(rememberSplitSceneStrategy()),
            transitionSpec = { ContentTransform(EnterTransition.None, ExitTransition.None) },
            popTransitionSpec = { ContentTransform(EnterTransition.None, ExitTransition.None) },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<WelcomeScreen> {
                    WelcomePage(
                        onLessonHeaderClick = { lesson ->
                            backStack.add(LessonScreen(lesson.id))
                        },
                    )
                }
                entry<LessonScreen>(
                    metadata = SplitSceneStrategy.split(),
                ) {
                    LessonPage(
                        id = it.id,
                        onLearnClick = { id ->
                            backStack.add(OpenQuestionScreen(id))
                        },
                        onBackClick = {
                            backStack.clear()
                            backStack.add(WelcomeScreen)
                        },
                    )
                }
                entry<OpenQuestionScreen> {
                    OpenQuestionPage(
                        id = it.id,
                        onBackClick = { backStack.removeLast() },
                    )
                }
            },
        )
    }
}

@Composable
internal fun WithAppGraph(
    clientSpellcheck: ClientSpellcheck,
    content: @Composable () -> Unit,
) {
    val graph = remember {
        createGraphFactory<AppGraph.Factory>().create(clientSpellcheck)
    }
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides graph.metroViewModelFactory,
    ) {
        content()
    }
}
