package com.github.terrakok.oefoef.ui.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.terrakok.oefoef.ui.common.Icons

@Composable
fun GymPage(
    onArticlesClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(onBackClick = onBackClick)
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 340.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 800.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    CategoryCard(
                        title = "Articles",
                        description = "Practice 'de', 'het', and 'een' in context.",
                        icon = Icons.ArticlesBook,
                        isActive = true,
                        onClick = onArticlesClick
                    )
                }
                item {
                    CategoryCard(
                        title = "Verbs Conjugation",
                        description = "Master Dutch verb conjugations across tenses.",
                        icon = Icons.VerbsShapes,
                        isActive = false
                    )
                }
                item {
                    CategoryCard(
                        title = "Word Order",
                        description = "Learn the correct placement of words in Dutch sentences.",
                        icon = Icons.WordOrder,
                        isActive = false
                    )
                }
                item {
                    CategoryCard(
                        title = "Vocabulary",
                        description = "Expand your Dutch word bank with thematic sets.",
                        icon = Icons.Translate,
                        isActive = false
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Practice Gym") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Back, contentDescription = "Back")
            }
        }
    )
}

@Composable
private fun CategoryCard(
    title: String,
    description: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isActive) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (isActive) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!isActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            modifier = Modifier.width(IntrinsicSize.Min),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "WIP",
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isActive) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                )
            }
        }
    }
}
