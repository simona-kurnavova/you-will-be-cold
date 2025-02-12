package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun IconTextRow(
    items: List<IconRowData>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SPACER_PADDING.dp)
    ) {
        items.forEach { (icon, text, textAttr, iconAttr) ->
            IconText(
                text = text,
                icon = icon,
                textAttr = textAttr ?: defaultSmallTextAttr(),
                iconAttr = iconAttr ?: defaultSmallIconAttr()
            )
        }
    }
}

private const val SPACER_PADDING = 8

data class IconRowData(
    val painter: Painter,
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
            items = listOf(
                IconRowData(
                    painter = rememberVectorPainter(Icons.Default.LocationOn),
                    text = "Location",
                ),
                IconRowData(
                    painter = rememberVectorPainter(Icons.Default.LocationOn),
                    text = "Location",
                )
            )
        )
    }
}
