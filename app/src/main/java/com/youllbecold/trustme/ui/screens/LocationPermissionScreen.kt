package com.youllbecold.trustme.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.OverlaySkeleton
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.LocationPermissionAction
import com.youllbecold.trustme.ui.viewmodels.LocationPermissionViewModel
import com.youllbecold.trustme.utils.IntentUtils
import com.youllbecold.trustme.utils.PermissionHelper
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationPermissionRoot(
    viewModel: LocationPermissionViewModel = koinViewModel(),
    navigateToDashboard: () -> Unit
) {
    LocationPermissionScreen { action ->
        when (action) {
            is LocationPermissionAction.LocationPermissionGranted -> navigateToDashboard()
            else -> viewModel.onAction(action)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissionScreen(
    onAction: (LocationPermissionAction) -> Unit,
) {
    val context = LocalContext.current

    val locationPermissionState = rememberMultiplePermissionsState(
        PermissionHelper.locationPermissions.toList()
    )

    val bgLocationPermissionState = rememberPermissionState(
        PermissionHelper.bgLocationPermission.first()
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                acc && isPermissionGranted
            }

            if (!permissionsGranted) {
                Toast.makeText(
                    context,
                    context.getString(R.string.location_permission_deny_toast), Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    LaunchedEffect(locationPermissionState.allPermissionsGranted, bgLocationPermissionState.status.isGranted) {
        onAction(LocationPermissionAction.RefreshLocationPermissionState)

        if (locationPermissionState.allPermissionsGranted && bgLocationPermissionState.status.isGranted) {
            onAction(LocationPermissionAction.LocationPermissionGranted)
        }
    }

    val action = {
        when {
            !locationPermissionState.allPermissionsGranted -> {
                if (locationPermissionState.shouldShowRationale) {
                    IntentUtils.openAppSettings(context)

                    Toast.makeText(
                        context,
                        context.getString(R.string.location_permission_ask_toast),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    locationPermissionLauncher.launch(PermissionHelper.locationPermissions)
                }
            }

            bgLocationPermissionState.status.shouldShowRationale ->
                bgLocationPermissionState.launchPermissionRequest()

            else -> {
                IntentUtils.openAppSettings(context)

                Toast.makeText(
                    context,
                    context.getString(R.string.location_permission_bg_ask_toast),
                    Toast.LENGTH_LONG
                ).show()            }
        }
    }

    val description = when {
        !locationPermissionState.allPermissionsGranted -> R.string.location_screen_description
        else -> R.string.location_screen_bg_description
    }

    OverlaySkeleton(
        title = R.string.location_screen_title,
        subtitle = description,
        buttonText = R.string.location_screen_grant_permission_button,
        action = action,
        image = R.drawable.location_purple,
    )
}

@Preview(showBackground = true)
@Composable
private fun LocationPermissionScreenPreview() {
    YoullBeColdTheme {
        LocationPermissionScreen {}
    }
}
