package com.youllbecold.trustme.settings.ui

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.ui.components.inputs.ToggleRow
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.settings.ui.model.SettingsUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToLogRemindSetup: () -> Unit,
    navigateToRecommendSetup: () -> Unit
) {
    SettingsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(),
        onAction = { action ->
            when(action) {
                is SettingsAction.SetupDailyNotification -> navigateToLogRemindSetup()
                is SettingsAction.SetupRecommendNotification -> navigateToRecommendSetup()
                else -> viewModel.onAction(action)
            }
        }
    )
}

/**
 * Settings screen.
 */
@Composable
private fun SettingsScreen(
    uiState: State<SettingsUiState>,
    onAction: (SettingsAction) -> Unit,
) {
    val context = LocalContext.current
    val state = uiState.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
    ) {
        Spacer(modifier = Modifier.height(SPACE_BETWEEN_TOGGLES.dp))

        ToggleRow(
            title = stringResource(R.string.settings_daily_notification),
            subtitle = stringResource(R.string.settings_daily_notification_subtitle),
            checked = state.allowDailyNotification,
            onChecked = { isChecked ->
                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    && !PermissionChecker.hasNotificationPermission(context)) {
                    onAction(SettingsAction.SetupDailyNotification)
                } else {
                    onAction(SettingsAction.SetAllowDailyNotification(isChecked))
                }
            },
        )
        
        Spacer(modifier = Modifier.height(SPACE_BETWEEN_TOGGLES.dp))

        ToggleRow(
            title = stringResource(R.string.settings_recommend_notification),
            subtitle = stringResource(R.string.settings_recommend_notification_subtitle),
            checked = state.allowRecommendNotification,
            onChecked = { isChecked ->
                @Suppress("ComplexCondition")
                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    && !PermissionChecker.hasNotificationPermission(context)
                    || !PermissionChecker.hasBgLocationPermission(context)
                ) {
                    onAction(SettingsAction.SetupRecommendNotification)
                } else {
                    onAction(SettingsAction.SetAllowRecommendNotification(isChecked))
                }
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
    }
}

private const val SPACE_BETWEEN_TOGGLES: Int = 16
private const val HORIZONTAL_SCREEN_PADDING: Int = 16

@Preview
@Composable
private fun SettingsScreenPreview() {
    YoullBeColdTheme {
        SettingsScreen(
            remember { mutableStateOf(SettingsUiState()) },
            {},
        )
    }
}
