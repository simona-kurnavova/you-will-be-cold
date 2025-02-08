package com.youllbecold.trustme.ui.components.generic

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IconText(
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    iconAttributes: IconAttributes? = null,
    paddingBeforeText: Int = PADDING_BETWEEN_ELEMENTS
) {
    Row {
        iconAttributes?.let {
            Icon(
                imageVector = ImageVector.vectorResource(it.icon),
                contentDescription = null,
                modifier = Modifier.size(it.iconSize.dp),
                tint = it.tint ?: MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.padding(paddingBeforeText.dp))

        Text(
            text = text,
            style = textStyle,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

data class IconAttributes(
    @DrawableRes val icon: Int,
    val iconSize: Int = DEFAULT_ICON_SIZE,
    val tint: Color? = null,
)

private const val DEFAULT_ICON_SIZE = 24
private const val PADDING_BETWEEN_ELEMENTS = 2

@Preview
@Composable
private fun IconTextPreview() {
    IconText(
        text = "Icon Text",
        iconAttributes = IconAttributes(
            icon = android.R.drawable.ic_menu_search,
            iconSize = 24,
        ),
    )
}