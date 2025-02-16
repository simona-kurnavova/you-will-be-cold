package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.youllbecold.trustme.utils.PermissionHelper
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for location permission screen.
 */
@KoinViewModel
class LocationPermissionViewModel(
    private val permissionHelper: PermissionHelper,
) : ViewModel() {
    init {
        refreshLocationPermissionState()
    }

    /**
     * Handles [LocationPermissionAction].
     */
    fun onAction(action: LocationPermissionAction) {
        when (action) {
            is LocationPermissionAction.RefreshLocationPermissionState ->
                refreshLocationPermissionState()
            is LocationPermissionAction.LocationPermissionGranted -> Unit // Handled by navigation
        }
    }

    /**
     * Refreshes location permission state.
     */
    private fun refreshLocationPermissionState() {
        permissionHelper.refreshLocationPermissionState()
    }
}

/**
 * Actions for [LocationPermissionViewModel].
 */
sealed class LocationPermissionAction {
    /**
     * Refreshes location permission state.
     */
    data object RefreshLocationPermissionState : LocationPermissionAction()

    /**
     * Location permission was granted - pop the screen.
     */
    data object LocationPermissionGranted : LocationPermissionAction()
}
