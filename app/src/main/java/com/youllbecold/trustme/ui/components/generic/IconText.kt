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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr

@Composable
fun IconText(
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    @DrawableRes icon: Int? = null,
    iconAttr: IconAttr = defaultSmallIconAttr(),
    paddingBeforeText: Int = PADDING_BETWEEN_ELEMENTS
) {
    Row {
        icon?.let {
            Icon(
                imageVector = ImageVector.vectorResource(it),
                contentDescription = iconAttr.contentDescription,
                modifier = Modifier.size(iconAttr.size.dp),
                tint = iconAttr.tint,
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

private const val PADDING_BETWEEN_ELEMENTS = 2

@Preview
@Composable
private fun IconTextPreview() {
    IconText(
        text = "Icon Text",
        icon = R.drawable.ic_cloud,
    )
}