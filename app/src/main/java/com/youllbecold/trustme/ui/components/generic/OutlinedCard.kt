package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.generic.attributes.BorderAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultBorderAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    borderAttr: BorderAttr = defaultBorderAttr(),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        border = BorderStroke(borderAttr.width.dp, borderAttr.color),
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
        OutlinedCard {
            Text(text = "This is an outlined card")
        }
    }
}
