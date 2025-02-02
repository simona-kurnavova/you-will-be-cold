package com.youllbecold.trustme.ui.components.generic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(0.1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier.padding(4.dp),
    ) {
        Box(
            modifier = modifier.padding(8.dp),
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedCardPreview() {
    YoullBeColdTheme {
        OutlinedCard(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "This is an outlined card")
        }
    }
}
