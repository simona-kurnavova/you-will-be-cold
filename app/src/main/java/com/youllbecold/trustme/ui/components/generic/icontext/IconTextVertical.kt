package com.youllbecold.trustme.ui.components.generic.icontext

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.ThemedIcon
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

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

        Text(
            text = title,
            style = titleAttr.style,
            color = titleAttr.color,
        )

        subtitle?.let {
            Spacer(modifier = Modifier.height(SPACE_INSIDE_ITEM.dp))

            Text(
                text = subtitle,
                style = subtitleAttr.style,
                color = subtitleAttr.color
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