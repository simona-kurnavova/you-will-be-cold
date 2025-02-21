package com.youllbecold.trustme.ui.components.cards

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun RecommendationCard(
    tmpText: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Text(tmpText)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecommendationCardPreview() {
    YoullBeColdTheme {
        RecommendationCard(tmpText = "Recommendation")
    }
}
