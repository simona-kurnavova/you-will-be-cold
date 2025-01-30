package com.youllbecold.trustme.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
 * @param checked current state of the toggle.
 * @param onChecked callback when the toggle is checked.
 */
@Composable
fun ToggleRow(
    text: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = text
        )

        Switch(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp),
            checked = checked,
            onCheckedChange = onChecked,
        )
    }
}

@Preview
@Composable
private fun ToggleRowPreview() {
    ToggleRow(
        text = "Toggle row",
        checked = true,
        onChecked = {},
    )
}
