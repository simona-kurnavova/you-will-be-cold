package com.youllbecold.trustme.info.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.section.Section
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun InfoScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = PADDING_BETWEEN_SECTIONS.dp)
            .padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
    ) {
        Section(
            title = stringResource(id = R.string.info_title_how_section),
        ) {
            ThemedText(
                text = stringResource(id = R.string.info_desc_how_section),
            )
        }

        Spacer(modifier = Modifier.height(PADDING_BETWEEN_SECTIONS.dp))

        Section(
            title = stringResource(id = R.string.info_title_why_section),
        ) {
            ThemedText(
                text = stringResource(id = R.string.info_desc_why_section),
            )
        }
    }
}

private const val PADDING_BETWEEN_SECTIONS = 8
private const val HORIZONTAL_SCREEN_PADDING = 16

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun InfoScreenPreview() {
    YoullBeColdTheme {
        InfoScreen()
    }
}
