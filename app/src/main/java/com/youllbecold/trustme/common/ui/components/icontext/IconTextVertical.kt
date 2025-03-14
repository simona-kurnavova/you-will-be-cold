package com.youllbecold.trustme.common.ui.components.icontext

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.attributes.IconAttr
import com.youllbecold.trustme.common.ui.attributes.TextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.common.ui.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.themed.ThemedIcon
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun IconTextVertical(
    title: String,
    iconType: IconType,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    iconAttr: IconAttr = defaultSmallIconAttr(),
    titleAttr: TextAttr = defaultMediumTextAttr(),
    subtitleAttr: TextAttr = defaultSmallTextAttr(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(INSIDE_PADDING.dp)
    ) {

        ThemedIcon(
            iconType = iconType,
            iconAttr = iconAttr,
        )

        Spacer(modifier = Modifier.height(SPACE_INSIDE_ITEM.dp))

        ThemedText(
            text = title,
            textAttr = titleAttr
        )

        subtitle?.let {
            Spacer(modifier = Modifier.height(SPACE_INSIDE_ITEM.dp))

            ThemedText(
                text = subtitle,
                textAttr = subtitleAttr,
            )
        }
    }
}

private const val INSIDE_PADDING = 4
private const val SPACE_INSIDE_ITEM = 4

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun IconTextVerticalPreview() {
    YoullBeColdTheme {
        IconTextVertical(
            title = "Title",
            iconType = IconType.Sun,
            subtitle = "Subtitle",
        )
    }
}