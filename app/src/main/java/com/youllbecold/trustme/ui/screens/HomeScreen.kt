package com.youllbecold.trustme.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

/**
 * Home screen.
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = "This is home screen",
        modifier = modifier
    )
}


@Preview
@Composable
fun HomeScreenPreview() {
    YoullBeColdTheme {
        HomeScreen()
    }
}
