package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.attributes.BorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.fadedBorderAttr
import com.youllbecold.trustme.ui.components.utils.rememberVector
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun ClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconType: IconType? = null,
    iconAttr: IconAttr = defaultSmallIconAttr(),
    textAttr: TextAttr = defaultSmallTextAttr(),
    borderAttr: BorderAttr = fadedBorderAttr(),
    cornerRadius: Int = ITEM_CORNER_RADIUS
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
        IconText(
            text = text,
            textAttr = textAttr,
            iconType = iconType,
            iconAttr = iconAttr,
        )
    }
}

private const val ITEM_CORNER_RADIUS = 12
private const val INSIDE_PADDING = 12

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ClickableTextPreview() {
    YoullBeColdTheme {
        ClickableText(
            text = "Clickable text",
            iconType = IconType.Cloud,
            onClick = {},
            modifier = Modifier
        )
    }
}
