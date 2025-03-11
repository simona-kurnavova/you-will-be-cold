package com.youllbecold.trustme.common.domain.usecases.location

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.location.LocationController

class FetchLocationUseCase(
    private val app: Application,
    private val locationController: LocationController
) {
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun fetchLocation(): GeoLocation? =
        locationController.simpleLocation
            ?: LocationController.getLastLocation(app)
}
