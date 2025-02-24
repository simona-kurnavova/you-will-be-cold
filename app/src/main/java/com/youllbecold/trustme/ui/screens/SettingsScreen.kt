package com.youllbecold.trustme.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.inputs.ToggleRow
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.SettingsAction
import com.youllbecold.trustme.ui.viewmodels.SettingsUiState
import com.youllbecold.trustme.ui.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(),
        onAction = viewModel::onAction
    )
}

/**
 * Settings screen.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    uiState: State<SettingsUiState>,
    onAction: (SettingsAction) -> Unit,
) {
    val context = LocalContext.current
    val state = uiState.value
    var dailyNotifRequested: Boolean by rememberSaveable { mutableStateOf(false) }
    var recommendNotifRequested: Boolean by rememberSaveable { mutableStateOf(false) }

    val notificationPermState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null // Not needed on lower APIs
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            when {
                dailyNotifRequested -> onAction(SettingsAction.SetAllowDailyNotification(true))
                recommendNotifRequested -> onAction(SettingsAction.SetAllowRecommendNotification(true))
            }
        } else {
            Toast.makeText(context, context.getString(R.string.toast_notification_perm_denied), Toast.LENGTH_SHORT).show()
        }
        dailyNotifRequested = false
        recommendNotifRequested = false
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(SPACE_BETWEEN_TOGGLES.dp))

        ToggleRow(
            title = stringResource(R.string.settings_daily_notification),
            subtitle = stringResource(R.string.settings_daily_notification_subtitle),
            checked = state.allowDailyNotification,
            onChecked = { isChecked ->
                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    && notificationPermState?.status?.isGranted == false) {
                    dailyNotifRequested = true
                    Toast.makeText(context, context.getString(R.string.toast_ask_notification_perm), Toast.LENGTH_SHORT).show()
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    && notificationPermState?.status?.isGranted == false) {
                    recommendNotifRequested = true
                    Toast.makeText(context, context.getString(R.string.toast_ask_notification_perm), Toast.LENGTH_SHORT).show()
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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

        // TODO: Add toggle for morning recommendation notification
    }
}

private const val SPACE_BETWEEN_TOGGLES: Int = 16

@Preview
@Composable
fun SettingsScreenPreview() {
    YoullBeColdTheme {
        SettingsScreen(
            remember { mutableStateOf(SettingsUiState()) },
            {},
        )
    }
}
