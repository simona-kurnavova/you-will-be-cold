package com.youllbecold.trustme.utils

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Singleton

/**
 * Utility class for permission checks.
 */
@Singleton
class PermissionHelper(private val app: Application){
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
        _hasLocationPermission.update { hasLocationPermission(app) }
    }

    companion object {
        /**
         * Location permissions required by the app.
         */
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        /**
         * Background location permission required by the app.
         */
        const val bgLocationPermission = Manifest.permission.ACCESS_BACKGROUND_LOCATION

        /**
         * Notification permission required by the app.
         */
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        const val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

        /**
         * Whether location permission is granted.
         *
         * Usage: Access of last devices location in order to localize weather.
         */
        fun hasLocationPermission(context: Context): Boolean =
            locationPermissions.all { hasPermission(context, it) }

        /**
         * Whether notification permission is granted.
         *
         * Usage: Showing notifications.
         */
        fun hasNotificationPermission(context: Context): Boolean =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    hasPermission(context, notificationPermission)

        /**
         * Whether background location permission is granted.
         *
         * Usage: Access of location on the background.
         */
        fun hasBgLocationPermission(context: Context): Boolean =
            hasPermission(context, bgLocationPermission)

        private fun hasPermission(context: Context, permission: String): Boolean =
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}
