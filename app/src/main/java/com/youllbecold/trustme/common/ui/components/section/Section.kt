package com.youllbecold.trustme.common.ui.components.section

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.components.themed.ThemedCard
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        SectionTitle(
            text = title,
        )

        ThemedCard {
            content()
        }

        Spacer(modifier = Modifier.height(PADDING_BETWEEN_SECTIONS.dp))
    }
}

private const val PADDING_BETWEEN_SECTIONS = 18

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SectionPreview() {
    YoullBeColdTheme {
        Section(title = "Section title") {
            ThemedText("Section content")
        }
    }
}
