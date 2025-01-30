package com.youllbecold.trustme.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

/**
 * History screen.
 */
@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    Text(
        text = "This is history screen",
        modifier = modifier
    )
}


@Preview
@Composable
fun HistoryScreenPreview() {
    YoullBeColdTheme {
        HistoryScreen()
    }
}
