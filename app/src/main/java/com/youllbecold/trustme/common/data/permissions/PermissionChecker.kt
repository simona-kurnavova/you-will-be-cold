package com.youllbecold.trustme.common.data.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

/**
 * Class for permission checks.
 */
object PermissionChecker {
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
     * Usage: Access of last device's location in order to localize weather.
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
