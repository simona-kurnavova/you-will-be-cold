package com.youllbecold.trustme.common.ui.components.inputs

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.attributes.TextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultSmallFadedTextAttr
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun ToggleRow(
    title: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    titleAttr: TextAttr = defaultMediumTextAttr(),
    subtitleAttr: TextAttr = defaultSmallFadedTextAttr(),
    onChecked: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(ITEM_CORNER_RADIUS.dp))
            .clickable { onChecked(!checked) }
            .padding(INTERNAL_PADDING.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = START_PADDING.dp)
                .weight(1f)
        ) {

            ThemedText(
                text = title,
                textAttr = titleAttr
            )

            if (subtitle != null) {
                ThemedText(
                    text = subtitle,
                    textAttr = subtitleAttr,
                )
            }
        }

        Switch(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = SWITCH_HORIZONTAL_PADDING.dp),
            checked = checked,
            onCheckedChange = null
        )
    }
}

private const val INTERNAL_PADDING = 12
private const val START_PADDING = 12
private const val SWITCH_HORIZONTAL_PADDING = 10
private const val ITEM_CORNER_RADIUS = 12

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ToggleRowPreview() {
    YoullBeColdTheme {
        Column {
            ToggleRow(
                title = "Title",
                subtitle = "Subtitle",
                checked = true,
                onChecked = {},
            )
        }
    }
}
