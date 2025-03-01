package com.youllbecold.trustme.ui.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.icontext.IconTextVertical
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.ThemedCard
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallTextAttr
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun ErrorCard(
    errorCardType: ErrorCardType,
    modifier: Modifier = Modifier
) {
    ThemedCard(
        bgColor = MaterialTheme.colorScheme.errorContainer,
        modifier = modifier
    ) {
        val text = when(errorCardType) {
            ErrorCardType.OFFLINE -> stringResource(R.string.offline_state_message)
            ErrorCardType.GENERIC -> stringResource(R.string.generic_error_message)
        }

        IconTextVertical(
            title = text,
            iconType = IconType.SadEmoji,
            titleAttr = defaultSmallTextAttr().copy(
                color = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = INSIDE_PADDING.dp),
        )
    }
}

private const val INSIDE_PADDING = 24

enum class ErrorCardType {
    OFFLINE,
    GENERIC
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun OfflineCardPreview() {
    YoullBeColdTheme {
        ErrorCard(
            errorCardType = ErrorCardType.OFFLINE
        )
    }
}