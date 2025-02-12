package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.attributes.BorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultBorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun Tile(
    title: String,
    @DrawableRes icon: Int,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(INSIDE_PADDING.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = iconAttr.contentDescription,
                modifier = Modifier.size(iconAttr.size.dp),
                tint = iconAttr.tint
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
}

private const val INSIDE_PADDING = 4
private const val SPACE_INSIDE_ITEM = 2

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TilePreview() {
    YoullBeColdTheme {
        Tile(
            title = "Title",
            icon = R.drawable.ic_sun,
            subtitle = "Subtitle",
            onClick = {}
        )
    }
}
