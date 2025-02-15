package com.youllbecold.trustme.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconTextVertical
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun OfflineCard(modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier) {
        IconTextVertical(
            title = stringResource(R.string.offline_state_message),
            iconType = IconType.SadEmoji,
            modifier = Modifier.padding(vertical = INSIDE_PADDING.dp),
        )
    }
}

private const val INSIDE_PADDING = 24


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun OfflineCardPreview() {
    YoullBeColdTheme {
        OfflineCard()
    }
}