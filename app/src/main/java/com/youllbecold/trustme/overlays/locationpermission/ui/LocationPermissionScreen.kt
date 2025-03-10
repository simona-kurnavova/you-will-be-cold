package com.youllbecold.trustme.overlays.locationpermission.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.ui.utils.IntentUtils
import com.youllbecold.trustme.overlays.ui.OverlaySkeleton
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
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
        PermissionChecker.locationPermissions.toList()
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

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        onAction(LocationPermissionAction.RefreshLocationPermissionState)

        if (locationPermissionState.allPermissionsGranted) {
            onAction(LocationPermissionAction.LocationPermissionGranted)
        }
    }

    val action = {
        if (locationPermissionState.shouldShowRationale) {
            IntentUtils.openAppSettings(context)

            Toast.makeText(
                context,
                context.getString(R.string.location_permission_ask_toast),
                Toast.LENGTH_LONG
            ).show()
        } else {
            locationPermissionLauncher.launch(PermissionChecker.locationPermissions)
        }
    }

    OverlaySkeleton(
        title = R.string.location_screen_title,
        subtitle = R.string.location_screen_description,
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
