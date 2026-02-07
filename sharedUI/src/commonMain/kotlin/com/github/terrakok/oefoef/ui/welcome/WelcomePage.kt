package com.github.terrakok.oefoef.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.terrakok.oefoef.LessonHeader
import com.github.terrakok.oefoef.ui.Icons
import com.github.terrakok.oefoef.ui.LoadingWidget
import dev.zacsweers.metrox.viewmodel.metroViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.github.terrakok.oefoef.ui.AppTheme
import oefoef.sharedui.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock

@Preview
@Composable
private fun WelcomePagePreview() {
    AppTheme {
        WelcomePageContent(
            items = listOf(
                LessonHeader("1", "Groeten & Voorstellen", "", 255, Clock.System.now()),
                LessonHeader("2", "Koffie Bestellen", "", 200, Clock.System.now()),
                LessonHeader("3", "De Weg Vragen", "", 310, Clock.System.now()),
                LessonHeader("4", "Boodschappen Doen", "", 405, Clock.System.now()),
            )
        )
    }
}


@Composable
fun WelcomePage(
    onLessonHeaderClick: (LessonHeader) -> Unit = {}
) {
    val vm = metroViewModel<WelcomeViewModel>()
    if (vm.loading || vm.error != null) {
        LoadingWidget(
            modifier = Modifier.fillMaxSize(),
            error = vm.error,
            loading = vm.loading,
            onReload = { vm.loadItems() }
        )
        return
    }

    WelcomePageContent(
        items = vm.items,
        onLessonHeaderClick = onLessonHeaderClick,
        onPullToRefresh = { vm.refresh() },
        isRefreshing = vm.isRefreshing,
    )
}

@Composable
fun WelcomePageContent(
    items: List<LessonHeader>,
    onLessonHeaderClick: (LessonHeader) -> Unit = {},
    isRefreshing: Boolean = false,
    onPullToRefresh: () -> Unit = {}
) {
    val state = rememberLazyGridState()
    val ptrState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = if (state.canScrollBackward) 8.dp else 0.dp,
            ) {
                WelcomeTopBar()
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        PullToRefreshBoxTouchOnly(
            isRefreshing = isRefreshing,
            onRefresh = onPullToRefresh,
            state = ptrState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp),
                    isRefreshing = isRefreshing,
                    state = ptrState,
                )
            }
        ) {
            LazyVerticalGrid(
                state = state,
                columns = GridCells.Adaptive(minSize = 300.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = paddingValues.plus(PaddingValues(horizontal = 16.dp, vertical = 48.dp)),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                item(
                    span = { GridItemSpan(maxCurrentLineSpan) }
                ) { Header() }
                items(items) { lesson ->
                    LessonCard(
                        lesson = lesson,
                        onClick = { onLessonHeaderClick(lesson) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Master Dutch through",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Real News and Podcasts",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Listen, watch, and answer the questions.\n" +
                    "Constantly new videos and questions!\n" +
                    "The app is free and open source!\n" +
                    "To support its operation and improve the service.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            val uriHandler = LocalUriHandler.current
            OutlinedButton(
                onClick = { uriHandler.openUri("https://github.com/terrakok/OefOef") },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Github,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "SOURCES")
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { uriHandler.openUri("https://github.com/terrakok/OefOef") }, //todo
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.DonateBox,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "DONATE")
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun WelcomeTopBar() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.systemBars.exclude(WindowInsets.navigationBars))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "OefOef",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun LessonCard(
    lesson: LessonHeader,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = lesson.previewUrl,
                contentDescription = lesson.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Duration badge
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = formatDuration(lesson.lengthSeconds),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }

        Text(
            text = lesson.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun formatDuration(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "$mins:${secs.toString().padStart(2, '0')}"
}