package com.youllbecold.trustme.ui.screens.overlays

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
import com.youllbecold.trustme.ui.components.OverlaySkeleton
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.utils.IntentUtils
import com.youllbecold.trustme.utils.PermissionHelper

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    updatePermissionState: () -> Unit
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        PermissionHelper.locationPermissions.toList()
    )

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        updatePermissionState()
    }

    if (locationPermissionState.allPermissionsGranted) {
        return
    }

    val context = LocalContext.current

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

    val action = {
        if (!locationPermissionState.shouldShowRationale) {
            locationPermissionLauncher.launch(PermissionHelper.locationPermissions)
        } else {
            IntentUtils.openAppSettings(context)
            Toast.makeText(
                context,
                context.getString(R.string.location_permission_ask_toast),
                Toast.LENGTH_LONG
            ).show()
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
