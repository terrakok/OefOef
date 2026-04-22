package com.github.terrakok.oefoef.ui.articlesgym

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.terrakok.oefoef.entity.ArticleAnswer
import com.github.terrakok.oefoef.entity.ArticleCheckState
import com.github.terrakok.oefoef.entity.ArticleChoice
import com.github.terrakok.oefoef.entity.ArticlesGymExercise
import com.github.terrakok.oefoef.entity.ParsedExplanation
import com.github.terrakok.oefoef.ui.common.Icons
import com.github.terrakok.oefoef.ui.common.LoadingWidget
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun ArticlesGymPage(
    onBackClick: () -> Unit = {},
) {
    val vm = metroViewModel<ArticlesGymViewModel>()

    if (vm.loading || vm.error != null) {
        LoadingWidget(
            modifier = Modifier.fillMaxSize(),
            error = vm.error,
            loading = vm.loading,
            onReload = { vm.loadData() },
        )
        return
    }

    val exercise = vm.currentExercise ?: return
    val exercises = vm.exercises ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            TopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            BottomBar(
                currentIndex = vm.currentExerciseIndex,
                totalCount = exercises.size,
                onPreviousClick = vm::previousExercise,
                onNextClick = vm::nextExercise,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fill in the missing articles (de, het, een) or select '-' if no article is needed. Click on the placeholders to select.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(bottom = 32.dp)
            )

            Box {
                var selectedPlaceholder by remember { mutableStateOf<Int?>(null) }
                var menuOffset by remember { mutableStateOf(DpOffset.Zero) }

                PassageWithPlaceholders(
                    passage = exercise.sentenceWithPlaceholders,
                    placeholderCount = exercise.placeholderCount,
                    onPlaceholderClick = { placeholderId, pressOffset ->
                        selectedPlaceholder = placeholderId
                        menuOffset = pressOffset
                    },
                    answers = vm.answers,
                )

                ArticleDropdownMenu(
                    expanded = selectedPlaceholder != null,
                    offset = menuOffset,
                    onDismissRequest = { selectedPlaceholder = null },
                    onArticleSelected = { choice ->
                        vm.onAnswerUpdated(selectedPlaceholder!! - 1, choice)
                        selectedPlaceholder = null
                    }
                )
            }

            AnimatedVisibility(
                visible = vm.isAllCorrect(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                SuccessSection(exercise)
            }
        }
    }
}

@Composable
private fun SuccessSection(exercise: ArticlesGymExercise) {
    Column(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "WELL DONE!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            color = Color(0xFF2E7D32),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        exercise.parsedExplanations.forEach { parsed ->
            ExplanationCard(parsed)
        }
    }
}

@Composable
private fun ExplanationCard(parsedExplanation: ParsedExplanation) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = parsedExplanation.answer,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = parsedExplanation.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        navigationIcon = {
            Row(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onBackClick() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Back,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    )
}

@Composable
private fun BottomBar(
    currentIndex: Int,
    totalCount: Int,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Button(
                enabled = currentIndex > 0,
                onClick = onPreviousClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Icon(
                    imageVector = Icons.ArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp).rotate(180f),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Text("${currentIndex + 1} / $totalCount")

            Button(
                enabled = currentIndex < totalCount - 1,
                onClick = onNextClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Icon(
                    imageVector = Icons.ArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun PassageWithPlaceholders(
    passage: String,
    placeholderCount: Int,
    onPlaceholderClick: (Int, DpOffset) -> Unit,
    answers: List<ArticleAnswer?>,
) {
    val lastPressedOffset = remember { mutableStateOf(DpOffset.Zero) }

    val annotatedString = remember(passage, placeholderCount, answers.joinToString()) {
        buildAnnotatedStringWithPlaceholders(passage, answers) { placeholderId ->
            onPlaceholderClick(placeholderId, lastPressedOffset.value)
        }
    }

    Text(
        text = annotatedString,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Medium,
            lineHeight = 40.sp,
        ),
        modifier = Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitFirstDown(requireUnconsumed = false)
                    lastPressedOffset.value = DpOffset(event.position.x.toDp(), event.position.y.toDp())
                }
            }
        }.widthIn(max = 600.dp)
            .padding(bottom = 32.dp)
    )
}

private fun buildAnnotatedStringWithPlaceholders(
    passage: String,
    answers: List<ArticleAnswer?>,
    onPlaceholderClick: (Int) -> Unit,
): AnnotatedString {
    val regex = Regex("""\(\?(\d+)\)""")
    var lastIndex = 0
    return buildAnnotatedString {
        regex.findAll(passage).forEach { match ->
            append(passage.substring(lastIndex, match.range.first))
            val placeholderId = match.groupValues[1].toInt()
            val answer = answers.getOrNull(placeholderId - 1)
            val textStyle = getSpanStyleForState(answer?.state)
            withLink(
                LinkAnnotation.Clickable(
                    tag = "placeholder",
                    styles = TextLinkStyles(style = textStyle),
                    linkInteractionListener = { onPlaceholderClick(placeholderId) }
                )
            ) {
                // Use NBSP instead of regular spaces to prevent line breaking
                val label = if (answer?.choice != null) {
                    "[\u00A0${answer.choice.value}\u00A0]"
                } else {
                    "[\u00A0?\u00A0]"
                }
                append(label)
            }
            lastIndex = match.range.last + 1
        }
        append(passage.substring(lastIndex))
    }
}

@Composable
private fun ArticleDropdownMenu(
    expanded: Boolean,
    offset: DpOffset,
    onDismissRequest: () -> Unit,
    onArticleSelected: (ArticleChoice) -> Unit,
) {
    Box(modifier = Modifier.offset(offset.x, offset.y)) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            ArticleChoice.values.forEach { choice ->
                DropdownMenuItem(
                    text = { Text(choice.value) },
                    onClick = { onArticleSelected(choice) }
                )
            }
        }
    }
}

private fun getSpanStyleForState(state: ArticleCheckState?): SpanStyle =
    when (state) {
        ArticleCheckState.Correct -> SpanStyle(
            background = Color(0xFFC8E6C9),
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2E7D32),
        )
        ArticleCheckState.Incorrect -> SpanStyle(
            background = Color(0xFFFFCDD2),
            fontWeight = FontWeight.Medium,
            color = Color(0xFFC62828),
        )
        ArticleCheckState.Pending, null -> SpanStyle(
            background = Color(0xFFFFFFE0),
            fontWeight = FontWeight.Thin,
            fontStyle = FontStyle.Italic,
        )
    }
