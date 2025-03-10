package com.youllbecold.trustme.common.ui.components.themed

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.common.ui.attributes.TextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun ThemedText(
    text: String,
    modifier: Modifier = Modifier,
    textAttr: TextAttr = defaultMediumTextAttr(),
) {
    Text(
        text = text,
        style = textAttr.style,
        color = textAttr.color,
        modifier = modifier
    )
}

@Preview
@Composable
private fun ThemedTextPreview() {
    YoullBeColdTheme {
        ThemedText(
            text = "Hello, World!"
        )
    }
}