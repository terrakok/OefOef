package com.github.terrakok.oefoef.ui.question

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp

private val wordRegex = Regex("""\p{L}+""")

internal fun Modifier.drawSpellingChecks(
    textLayoutResult: TextLayoutResult?,
    rawText: String,
    wordsToHighlight: List<String>,
    ignoreCase: Boolean = true,
) = this.drawBehind {
    val layout = textLayoutResult ?: return@drawBehind
    if (rawText.isBlank() || wordsToHighlight.isEmpty()) return@drawBehind

    val highlightSet = if (ignoreCase) {
        wordsToHighlight.asSequence().map { it.lowercase() }.toSet()
    } else {
        wordsToHighlight.toSet()
    }

    val waveYOff = 2.dp.toPx()
    val waveLength = 4.dp.toPx()
    val waveHeight = 1.5.dp.toPx()
    val strokeWidth = 1.2.dp.toPx()
    val bottomPad = 1.dp.toPx()

    val textLen = rawText.length

    wordRegex.findAll(rawText).forEach { match ->
        val word = if (ignoreCase) match.value.lowercase() else match.value
        if (word !in highlightSet) return@forEach

        val start = match.range.first.coerceIn(0, textLen)
        val endExclusive = (match.range.last + 1).coerceIn(0, textLen)
        if (start >= endExclusive) return@forEach

        val firstLine = layout.getLineForOffset(start)
        val lastLine = layout.getLineForOffset(endExclusive - 1)

        for (lineIndex in firstLine..lastLine) {
            val lineStart = layout.getLineStart(lineIndex)
            val lineEnd = layout.getLineEnd(lineIndex)

            val segStart = maxOf(start, lineStart)
            val segEnd = minOf(endExclusive, lineEnd)
            if (segStart >= segEnd) continue

            val x1 = layout.getHorizontalPosition(segStart, usePrimaryDirection = false)
            val x2 = layout.getHorizontalPosition(segEnd, usePrimaryDirection = false)
            val startX = minOf(x1, x2)
            val endX = maxOf(x1, x2)

            val bottom = layout.getLineBottom(lineIndex)
            val baseline = layout.getLineBaseline(lineIndex)
            val waveY = (baseline + waveYOff).coerceAtMost(bottom - bottomPad)

            val path = Path().apply {
                moveTo(startX, waveY)
                var x = startX
                var down = true
                while (x < endX) {
                    val nextX = minOf(x + waveLength / 2f, endX)
                    val midX = (x + nextX) / 2f
                    val cpY = if (down) waveY + waveHeight else waveY - waveHeight
                    quadraticTo(midX, cpY, nextX, waveY)
                    x = nextX
                    down = !down
                }
            }

            drawPath(
                path = path,
                color = Color.Red,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
