package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.ToggleRow
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Settings screen.
 */
@Composable
fun SettingsScreen(
    allowDailyNotification: Flow<Boolean>,
    setAllowDailyNotification: (Boolean) -> Unit,
) {
    val state = allowDailyNotification.collectAsStateWithLifecycle(false)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ToggleRow(
            text = stringResource(R.string.settings_daily_notification),
            subtitle = stringResource(R.string.settings_daily_notification_subtitle),
            checked = state.value,
            onChecked = setAllowDailyNotification,
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    YoullBeColdTheme {
        SettingsScreen(
            flow { emit(true) },
            {},
        )
    }
}
