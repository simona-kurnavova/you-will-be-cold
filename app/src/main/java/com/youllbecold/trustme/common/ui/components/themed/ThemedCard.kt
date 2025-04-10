package com.youllbecold.trustme.common.ui.components.themed

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun ThemedCard(
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = bgColor,
        ),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(INSIDE_PADDING.dp)
                .fillMaxWidth(),
        ) {
            content()
        }
    }
}

private const val INSIDE_PADDING = 12

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun OutlinedCardPreview() {
    YoullBeColdTheme {
        ThemedCard {
            ThemedText(text = "This is an outlined card")
        }
    }
}
