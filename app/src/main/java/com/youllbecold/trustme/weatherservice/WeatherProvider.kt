package com.youllbecold.trustme.weatherservice

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.LocationState
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.trustme.weatherservice.internal.WeatherRepository
import com.youllbecold.trustme.weatherservice.internal.WeatherStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WeatherProvider(
    private val app: Application,
    private val weatherRepository: WeatherRepository,
    private val locationHelper: LocationHelper
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Current weather.
     */
    val currentWeather: StateFlow<WeatherStats?>
        get() = weatherRepository.currentWeather

    init {
        coroutineScope.launch {
            locationHelper.locationState.collectLatest { locationState ->
                if (locationState is LocationState.Success) {
                    val loc = locationState.location
                    weatherRepository.getForecast(loc.latitude.toFloat(), loc.longitude.toFloat())
                }
            }
        }
    }

    @SuppressLint("MissingPermission") // Solved by using PermissionHelper.hasLocationPermission
    fun loadWeather() {
        if (!PermissionHelper.hasLocationPermission(app)) {
            Log.d("WeatherProvider", "loadWeather: missing location permission")
            return
        }

        // Trigger location refresh, in case of successful, weather is refreshed automatically.
        locationHelper.refreshLocation(app)
    }
}
