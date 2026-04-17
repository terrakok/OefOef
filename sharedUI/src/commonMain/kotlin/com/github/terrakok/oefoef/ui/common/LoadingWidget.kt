package com.github.terrakok.oefoef.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoadingWidgetPreview() {
    AppTheme {
        LoadingWidget(
            modifier = Modifier.size(400.dp, 200.dp),
            error = "Error",
            loading = false,
            onReload = {},
        )
    }
}

@Composable
fun LoadingWidget(
    modifier: Modifier = Modifier,
    error: String?,
    loading: Boolean,
    onReload: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 400.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = error,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedButton(onClick = onReload) { Text("Reload") }
                }
            }
        }
    }
}
