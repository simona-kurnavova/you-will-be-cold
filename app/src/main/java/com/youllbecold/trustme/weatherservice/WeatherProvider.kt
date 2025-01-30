package com.youllbecold.trustme.weatherservice

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.LocationState
import com.youllbecold.trustme.utils.PermissionUtils
import kotlinx.coroutines.delay

class WeatherProvider(
    private val app: Application,
    private val weatherRepository: WeatherRepository,
    private val locationHelper: LocationHelper
) {
    val currentWeather by weatherRepository::currentWeather

    suspend fun loadWeather() {
        // TODO: get location from location service
        if (PermissionUtils.hasLocationPermission(app)) {
            Log.d("WeatherProvider", "loadWeather: has location permission")
            locationHelper.refreshLocation(app)
            delay(2000)
            (locationHelper.locationState.value as? LocationState.Success)?.let { state ->
                val loc = state.location
                val result = weatherRepository.getForecast(loc.latitude.toFloat(), loc.longitude.toFloat())
                Log.d("WeatherProvider", "loadWeather with real location: $result")
            }

        } else {
            Log.d("WeatherProvider", "loadWeather: no location permission")
            val latitude: Float = 50.0755f
            val longitude: Float = 14.4378f
            val result = weatherRepository.getForecast(latitude, longitude)
            Log.d("WeatherProvider", "loadWeather with fake location: $result")
        }
    }
}