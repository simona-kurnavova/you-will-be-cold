package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun IconText(
    text: String,
    modifier: Modifier = Modifier,
    textAttr: TextAttr = defaultSmallTextAttr(),
    icon: Painter? = null,
    iconAttr: IconAttr = defaultSmallIconAttr(),
    paddingBeforeText: Int = PADDING_BETWEEN_ELEMENTS,
) {
    Row(modifier = modifier) {
        icon?.let {
            Icon(
                painter = it,
                contentDescription = iconAttr.contentDescription,
                modifier = Modifier
                    .size(iconAttr.size.dp)
                    .align(Alignment.CenterVertically),
                tint = iconAttr.tint,
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

private const val PADDING_BETWEEN_ELEMENTS = 4

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun IconTextPreview() {
    YoullBeColdTheme {
        IconText(
            text = "Icon Text",
            icon = rememberVectorPainter(image = Icons.Default.LocationOn),
        )
    }
}