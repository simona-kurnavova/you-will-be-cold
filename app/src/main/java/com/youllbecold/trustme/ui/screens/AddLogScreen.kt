package com.youllbecold.trustme.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun AdLogRoot() {
    AddLogScreen()
}

@Composable
fun AddLogScreen() {
    Text(text = "Ad Log Screen")
}

@Preview(showBackground = true)
@Composable
fun AdLogScreenPreview() {
    YoullBeColdTheme {
        AddLogScreen()
    }
}