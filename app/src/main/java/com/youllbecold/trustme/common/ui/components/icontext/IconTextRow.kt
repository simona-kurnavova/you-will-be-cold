package com.youllbecold.trustme.common.ui.components.icontext

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.attributes.IconAttr
import com.youllbecold.trustme.common.ui.attributes.TextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.common.ui.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun IconTextRow(
    items: PersistentList<IconRowData>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SPACER_PADDING.dp)
    ) {
        items.forEach { (icon, text, textAttr, iconAttr) ->
            IconText(
                text = text,
                iconType = icon,
                textAttr = textAttr ?: defaultSmallTextAttr(),
                iconAttr = iconAttr ?: defaultSmallIconAttr()
            )
        }
    }
}

private const val SPACER_PADDING = 8

@Stable
data class IconRowData(
    val iconType: IconType,
    val text: String,
    val textAttr: TextAttr? = null,
    val iconAttr: IconAttr? = null
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun IconTextRowPreview() {
    YoullBeColdTheme {
         IconTextRow(
            items = persistentListOf(
                IconRowData(
                    iconType = IconType.Location,
                    text = "Location",
                ),
                IconRowData(
                    iconType = IconType.Lightning,
                    text = "Location",
                )
            )
        )
    }
}
