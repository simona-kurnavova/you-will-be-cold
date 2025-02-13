package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.ToggleRow
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.SettingsAction
import com.youllbecold.trustme.ui.viewmodels.SettingsUiState
import com.youllbecold.trustme.ui.viewmodels.SettingsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction
    )
}

/**
 * Settings screen.
 */
@Composable
fun SettingsScreen(
    uiState: Flow<SettingsUiState>,
    onAction: (SettingsAction) -> Unit,
) {
    val state by uiState.collectAsStateWithLifecycle(SettingsUiState())

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(SPACE_BETWEEN_TOGGLES.dp))

        ToggleRow(
            title = stringResource(R.string.settings_daily_notification),
            subtitle = stringResource(R.string.settings_daily_notification_subtitle),
            checked = state.allowDailyNotification,
            onChecked = { isChecked ->
                onAction(SettingsAction.SetAllowDailyNotification(isChecked))
            },
        )
        
        Spacer(modifier = Modifier.height(SPACE_BETWEEN_TOGGLES.dp))

        ToggleRow(
            title = stringResource(R.string.settings_use_celsius),
            subtitle = stringResource(R.string.settings_use_celsius_subtitle),
            checked = state.useCelsiusUnits,
            onChecked = { isChecked ->
                onAction(SettingsAction.SetUseCelsiusUnits(isChecked))
            }
        )

        // TODO: Add toggle for morning recommendation notification
    }
}

private const val SPACE_BETWEEN_TOGGLES: Int = 16

@Preview
@Composable
fun SettingsScreenPreview() {
    YoullBeColdTheme {
        SettingsScreen(
            flow { emit(SettingsUiState()) },
            {},
        )
    }
}
