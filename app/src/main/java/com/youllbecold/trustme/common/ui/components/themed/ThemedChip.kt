package com.youllbecold.trustme.common.ui.components.themed

import android.content.res.Configuration
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun ThemedChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    InputChip(
        onClick = onClick,
        label = { Text(text) },
        selected = selected,
        modifier = modifier.wrapContentSize(),
        colors = InputChipDefaults.inputChipColors().copy(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ThemedChipPreview() {
    YoullBeColdTheme {
        ThemedChip(
            text = "Today",
            onClick = {}
        )
    }
}
