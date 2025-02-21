package com.youllbecold.trustme.ui.components.generic

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThemedDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onBackground,
        thickness = DIVIDER_THICKNESS.dp,
        modifier = modifier.padding(vertical = DIVIDER_PADDING.dp)
    )
}

private const val DIVIDER_THICKNESS = 0.1f
private const val DIVIDER_PADDING = 8
