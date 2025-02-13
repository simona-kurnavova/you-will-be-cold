package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.attributes.BorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.fadedBorderAttr
import com.youllbecold.trustme.ui.components.utils.rememberVector
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun LabeledClickableText(
    label: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    textAttr: TextAttr = defaultSmallTextAttr(),
    borderAttr: BorderAttr = fadedBorderAttr(),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = textAttr.style,
            color = textAttr.color,
        )

        Spacer(modifier = Modifier.width(PADDING_UNDER_LABEL.dp))

        ClickableText(
            text = text,
            onClick = onClick,
            textAttr = textAttr,
            borderAttr = borderAttr,
            painter = painter,
        )
    }
}

private const val PADDING_UNDER_LABEL = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun LabeledClickableTextPreview() {
    YoullBeColdTheme {
        LabeledClickableText(
            label = "Label",
            text = "Clickable text",
            painter = rememberVector(R.drawable.ic_cloud),
            onClick = {},
        )
    }
}
