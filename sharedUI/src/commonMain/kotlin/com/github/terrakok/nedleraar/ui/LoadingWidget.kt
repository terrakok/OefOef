package com.github.terrakok.nedleraar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
            onReload = {}
        )
    }
}

@Composable
fun LoadingWidget(
    modifier: Modifier = Modifier,
    error: String?,
    loading: Boolean,
    onReload: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 400.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedButton(onClick = onReload) { Text("Reload") }
                }
            }
        }
    }
}