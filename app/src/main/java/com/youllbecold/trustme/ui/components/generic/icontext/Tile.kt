package com.youllbecold.trustme.ui.components.generic.icontext

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
import com.youllbecold.trustme.ui.components.generic.attributes.BorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultBorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun Tile(
    title: String,
    iconType: IconType,
    modifier: Modifier = Modifier,
    iconAttr: IconAttr = defaultSmallIconAttr(),
    titleAttr: TextAttr = defaultMediumTextAttr(),
    subtitleAttr: TextAttr = defaultSmallTextAttr(),
    borderAttr: BorderAttr = defaultBorderAttr(),
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    OutlinedCard(
        borderAttr = borderAttr,
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
    ) {
        IconTextVertical(
            title = title,
            iconType = iconType,
            subtitle = subtitle,
            iconAttr = iconAttr,
            titleAttr = titleAttr,
            subtitleAttr = subtitleAttr
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TilePreview() {
    YoullBeColdTheme {
        Tile(
            title = "Title",
            iconType = IconType.Sun,
            subtitle = "Subtitle",
            onClick = {}
        )
    }
}
