package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    useCelsius: Flow<Boolean>,
    setUseCelsius: (Boolean) -> Unit,
) {
    val allowDailyNotificationState =
        allowDailyNotification.collectAsStateWithLifecycle(false)

    val useCelsiusState =
        useCelsius.collectAsStateWithLifecycle(false)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ToggleRow(
            text = stringResource(R.string.settings_daily_notification),
            subtitle = stringResource(R.string.settings_daily_notification_subtitle),
            checked = allowDailyNotificationState.value,
            onChecked = setAllowDailyNotification,
        )
        
        Spacer(modifier = Modifier.padding(8.dp))

        ToggleRow(
            text = stringResource(R.string.settings_use_celsius),
            subtitle = stringResource(R.string.settings_use_celsius_subtitle),
            checked = useCelsiusState.value,
            onChecked = setUseCelsius,
        )

        // TODO: Add toggle for dark mode

        // TODO: Add toggle for morning recommendation notification
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    YoullBeColdTheme {
        SettingsScreen(
            flow { emit(true) },
            {},
            flow { emit(true) },
            {},
        )
    }
}
