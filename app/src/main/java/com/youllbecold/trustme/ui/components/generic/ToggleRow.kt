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

/**
 * Component for toggle with title.
 *
 * @param text title of the toggle.
 * @param subtitle subtitle of the toggle. Optional
 * @param checked current state of the toggle.
 * @param onChecked callback when the toggle is checked.
 */
@Composable
fun ToggleRow(
    text: String,
    subtitle: String? = null,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp)
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
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 3,
                )
            }
        }

        Switch(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp),
            checked = checked,
            onCheckedChange = onChecked,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ToggleRowPreview() {
    Column {
        ToggleRow(
            text = "Toggle row",
            subtitle = "subtitle of toggle row, some very long text that should wrap around",
            checked = true,
            onChecked = {},
        )
    }
}
