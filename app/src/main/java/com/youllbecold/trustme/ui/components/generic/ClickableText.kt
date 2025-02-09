package com.youllbecold.trustme.ui.components.generic

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.generic.attributes.BorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.fadedBorderAttr

@Composable
fun ClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    cornerRadius: Int = ITEM_CORNER_RADIUS,
    borderAttr: BorderAttr = fadedBorderAttr()
) {
    val shape = RoundedCornerShape(cornerRadius.dp)

    Box(
        modifier = modifier
            .clip(shape = shape)
            .border(
                width = borderAttr.width.dp,
                color = borderAttr.color,
                shape = shape
            )
            .clickable(
                onClick = { onClick() }
            )
            .padding(INSIDE_PADDING.dp)
    ) {
        Text(
            text = text,
            style = textStyle
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
