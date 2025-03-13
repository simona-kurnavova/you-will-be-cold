package com.youllbecold.trustme.common.ui.components.icontext

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.attributes.IconAttr
import com.youllbecold.trustme.common.ui.attributes.TextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.common.ui.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.common.ui.attributes.withAlpha
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.themed.ThemedIcon
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun IconText(
    text: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
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

        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {

            ThemedText(
                text = text,
                textAttr = textAttr,
            )

            subtitle?.let {
                ThemedText(
                    text = it,
                    textAttr = textAttr.withAlpha(alpha = SUBTITLE_ALPHA),
                )
            }
        }

    }
}

private const val PADDING_BETWEEN_ELEMENTS = 8
private const val SUBTITLE_ALPHA = 0.6f

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun IconTextPreview() {
    YoullBeColdTheme {
        IconText(
            text = "Icon Text",
            subtitle = "Subtitle",
            iconType = IconType.Location,
        )
    }
}