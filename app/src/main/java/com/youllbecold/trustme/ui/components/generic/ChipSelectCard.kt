package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun ChipSelectCard(
    options: List<String>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: Int = 0,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SPACE_BETWEEN_CHIPS.dp)
        ) {
            options.forEachIndexed { index, option ->
                ThemedChip(
                    text = option,
                    onClick = { onOptionSelected(index) },
                    selected = index == selectedOption
                )
            }
        }
        
        Spacer(modifier = Modifier.height(PADDING_UNDER_CHIPS.dp))

        content()
    }
}

private const val SPACE_BETWEEN_CHIPS = 4
private const val PADDING_UNDER_CHIPS = 4

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ChipSelectCardPreview() {
    YoullBeColdTheme {
        ChipSelectCard(
            options = listOf("Option 1", "Option 2", "Option 3"),
            onOptionSelected = {},
            selectedOption = 1,
            content = {
                Text("Content")
            }
        )
    }
}
