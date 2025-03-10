package com.youllbecold.trustme.common.data.permissions

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Singleton

/**
 * Utility class for permission checks.
 */
@Singleton
class LocationPermissionManager(private val app: Application) {
    private val _hasLocationPermission: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * Flow of the current location permission state.
     */
    val hasLocationPermission: StateFlow<Boolean> = _hasLocationPermission

    init {
        refreshLocationPermissionState()
    }

    /**
     * Refresh the location permission state.
     */
    fun refreshLocationPermissionState() {
        _hasLocationPermission.update { PermissionChecker.hasLocationPermission(app) }
    }
}
