package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun IconText(
    text: String,
    modifier: Modifier = Modifier,
    textAttr: TextAttr = defaultSmallTextAttr(),
    iconType: IconType? = null,
    iconAttr: IconAttr = defaultSmallIconAttr(),
    paddingBeforeText: Int = PADDING_BETWEEN_ELEMENTS,
) {
    Row(modifier = modifier) {
        iconType?.let {
            ThemedIcon(
                iconType = it,
                iconAttr = iconAttr,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }

        Spacer(modifier = Modifier.width(paddingBeforeText.dp))

        Text(
            text = text,
            style = textAttr.style,
            color = textAttr.color,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

private const val PADDING_BETWEEN_ELEMENTS = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun IconTextPreview() {
    YoullBeColdTheme {
        IconText(
            text = "Icon Text",
            iconType = IconType.Location,
        )
    }
}