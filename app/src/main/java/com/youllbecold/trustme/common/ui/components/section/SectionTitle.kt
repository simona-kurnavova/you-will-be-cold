package com.youllbecold.trustme.common.ui.components.section

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.attributes.TextAttr
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemedText(
            text = text,
            textAttr = TextAttr(
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = modifier
        )

        Spacer(modifier = Modifier.height(PADDING_UNDER_TITLE.dp))
    }
}

private const val PADDING_UNDER_TITLE = 18

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SectionTitlePreview() {
    YoullBeColdTheme {
        SectionTitle(text = "Section Title")
    }
}