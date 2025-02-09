package com.youllbecold.trustme.ui.components.generic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ToggleRow(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onChecked: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = START_PADDING.dp)
                .weight(1f),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = SUBTITLE_ALPHA),
                    maxLines = SUBTITLE_MAX_LINES,
                )
            }
        }

        Switch(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = SWITCH_HORIZONTAL_PADDING.dp),
            checked = checked,
            onCheckedChange = onChecked,
        )
    }
}

private const val START_PADDING = 12
private const val SUBTITLE_ALPHA = 0.7f
private const val SUBTITLE_MAX_LINES = 3
private const val SWITCH_HORIZONTAL_PADDING = 8

@Preview(showBackground = true)
@Composable
private fun ToggleRowPreview() {
    Column {
        ToggleRow(
            text = "Title",
            subtitle = "Subtitle",
            checked = true,
            onChecked = {},
        )
    }
}
