package com.youllbecold.trustme.ui.components.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(ITEM_CORNER_RADIUS.dp)) // Clips the clickable area to match the shape
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(ITEM_CORNER_RADIUS.dp)
            )
            .clickable(
                onClick = {
                    onClick()
                })
            .padding(INSIDE_PADDING.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

private const val ITEM_CORNER_RADIUS = 12
private const val INSIDE_PADDING = 8


@Preview(showBackground = true)
@Composable
private fun ClickableTextPreview() {
    ClickableText(
        text = "Clickable text",
        onClick = {},
        modifier = Modifier
    )
}