package com.youllbecold.trustme.common.domain.location

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.location.LocationController

/**
 * Use case for fetching the current location.
 */
class GeoLocationProvider(
    private val app: Application,
    private val locationController: LocationController
) {
    /**
     * Fetches the current location.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun fetchLocation(): GeoLocation? =
        locationController.simpleLocation
            ?: LocationController.getLastLocation(app)
}
