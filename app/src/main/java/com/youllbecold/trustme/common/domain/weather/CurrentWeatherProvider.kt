package com.youllbecold.trustme.common.domain.weather

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.model.WeatherModel

/**
 * Use case for fetching and refreshing the current weather.
 */
class CurrentWeatherProvider(
    private val app: Application,
    private val weatherRepository: WeatherRepository,
    private val locationController: LocationController,
    private val networkStatusProvider: NetworkStatusProvider
) {
    /**
     * Fetches the current weather for the current location.
     *
     * @param useCelsius Whether to use Celsius units.
     */
    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentWeather(useCelsius: Boolean): WeatherWithStatus {
        when {
            !networkStatusProvider.hasInternet() ->
                return WeatherWithStatus(status = LoadingStatus.NoInternet)
            !PermissionChecker.hasLocationPermission(app) ->
                return WeatherWithStatus(status = LoadingStatus.MissingPermission)
        }

        val location = locationController.quickGetLastLocation()
            ?: return WeatherWithStatus(status = LoadingStatus.GenericError)

        val result = weatherRepository.getCurrentWeather(
            location.latitude,
            location.longitude,
            useCelsius
        ).getOrNull()

        return WeatherWithStatus(
            status = if (result != null) LoadingStatus.Success else LoadingStatus.GenericError,
            weatherModel = result,
            location = location
        )
    }
}

/**
 * Data class for the current weather with status.
 */
data class WeatherWithStatus(
    val status: LoadingStatus = LoadingStatus.Idle,
    val weatherModel: WeatherModel? = null,
    val location: GeoLocation? = null
)
