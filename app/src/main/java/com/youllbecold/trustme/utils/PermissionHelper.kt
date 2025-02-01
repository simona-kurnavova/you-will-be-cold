package com.youllbecold.trustme.utils

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Singleton

/**
 * Utility class for permission checks.
 */
@Singleton
class PermissionHelper(
    private val app: Application
){

    private val _locationState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * Flow of the current location permission state.
     */
    val locationState: StateFlow<Boolean> = _locationState

    init {
        refreshLocationPermissionState()
    }

    /**
     * Refresh the location permission state.
     */
    fun refreshLocationPermissionState() {
        _locationState.value = hasLocationPermission(app)
    }

    companion object {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        /**
         * Whether location permission is granted.
         */
        fun hasLocationPermission(context: Context): Boolean =
            locationPermissions.all { hasPermission(context, it) }


        /**
         * Whether notification permission is granted.
         */
        fun hasNotificationPermission(context: Context): Boolean =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    hasPermission(context, Manifest.permission.POST_NOTIFICATIONS)

        private fun hasPermission(context: Context, permission: String): Boolean =
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}
