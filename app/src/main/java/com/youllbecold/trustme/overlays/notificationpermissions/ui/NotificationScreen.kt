package com.youllbecold.trustme.overlays.notificationpermissions.ui

import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.ui.utils.IntentUtils
import com.youllbecold.trustme.overlays.ui.OverlaySkeleton
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationScreenRoot(
    viewModel: NotificationViewModel = koinViewModel(),
    notification: NotificationType,
    navigateBack: () -> Unit
) {
    NotificationScreen(
        enableNotification = {
            when (notification) {
                NotificationType.RECOMMEND -> {
                    viewModel.onAction(NotificationAction.SetAllowRecommendNotification)
                    navigateBack()
                }

                NotificationType.LOG_REMINDER -> {
                    viewModel.onAction(NotificationAction.SetAllowDailyNotification)
                    navigateBack()
                }
            }
        },
        askForBgLocation = notification == NotificationType.RECOMMEND
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationScreen(
    enableNotification: () -> Unit,
    askForBgLocation: Boolean
) {
    val context = LocalContext.current

    val notificationPermState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(PermissionChecker.notificationPermission)
    } else {
        null // Not needed on lower APIs
    }

    val bgLocationPermissionState = if (askForBgLocation) {
        rememberPermissionState(PermissionChecker.bgLocationPermission)
    } else {
        null // Didn't ask for it
    }

    val notificationPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, context.getString(R.string.toast_notification_perm_denied), Toast.LENGTH_SHORT).show()
        }
    }

    val notificationGranted = notificationPermState?.status?.isGranted == true || notificationPermState == null
    val bgLocationGranted = bgLocationPermissionState?.status?.isGranted == true || bgLocationPermissionState == null

    LaunchedEffect(notificationGranted, bgLocationGranted) {
        if (notificationGranted && bgLocationGranted) {
            enableNotification()
        }
    }

    val (title, subtitle) = when {
        !notificationGranted -> R.string.notification_perm_title to R.string.notification_perm_descr
        else -> R.string.notification_bg_loc_perm_title to R.string.notification_bg_loc_perm_descr
    }

    OverlaySkeleton(
        title = title,
        subtitle = subtitle,
        buttonText = R.string.notification_grant_permission,
        action = {
            when {
                !notificationGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                    notificationPermLauncher.launch(PermissionChecker.notificationPermission)

                bgLocationPermissionState?.status?.shouldShowRationale == true ->
                    bgLocationPermissionState.launchPermissionRequest()

                askForBgLocation -> {
                    IntentUtils.openAppSettings(context)
                    Toast.makeText(
                        context,
                        context.getString(R.string.location_permission_ask_toast),
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> enableNotification()
            }
        },
        modifier = Modifier,
        image = R.drawable.ic_snowflake
    )
}

enum class NotificationType {
    LOG_REMINDER,
    RECOMMEND
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun NotificationScreenPreview() {
    YoullBeColdTheme {
        NotificationScreen(
            enableNotification = {},
            askForBgLocation = true
        )
    }
}
