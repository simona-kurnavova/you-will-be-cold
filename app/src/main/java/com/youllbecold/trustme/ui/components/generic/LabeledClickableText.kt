package com.youllbecold.trustme.ui.components.generic

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.attributes.BorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.fadedBorderAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun LabeledClickableText(
    label: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    borderAttr: BorderAttr = fadedBorderAttr(),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
        )

        Spacer(modifier = Modifier.padding(PADDING_UNDER_LABEL.dp))

        ClickableText(
            text = text,
            onClick = onClick,
            textStyle = textStyle,
            borderAttr = borderAttr,
            icon = icon,
        )
    }
}

private const val PADDING_UNDER_LABEL = 4

@Preview
@Composable
private fun LabeledClickableTextPreview() {
    YoullBeColdTheme {
        LabeledClickableText(
            label = "Label",
            text = "Clickable text",
            icon = R.drawable.ic_cloud,
            onClick = {},
        )
    }
}
