package com.youllbecold.trustme.common.domain.weather

import android.annotation.SuppressLint
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.domain.weather.utils.WeatherPrerequisitesChecker
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.api.isSuccessful
import com.youllbecold.weather.model.WeatherModel

/**
 * Provider for fetching and refreshing the current weather.
 */
class CurrentWeatherProvider(
    private val weatherRepository: WeatherRepository,
    private val locationController: LocationController,
) {
    /**
     * Fetches the current weather for the current location.
     *
     * @param useCelsius Whether to use Celsius units.
     */
    @SuppressLint("MissingPermission") // Checked in prerequisites
    suspend fun fetchCurrentWeather(useCelsius: Boolean): WeatherWithStatus {
        WeatherPrerequisitesChecker.check().let {
            if (it != Success) {
                return WeatherWithStatus(status = it)
            }
        }

        val location = locationController.quickGetLastLocation()
            ?: return WeatherWithStatus(status = Error.LocationMissing)

        val result = weatherRepository.getCurrentWeather(
            location.latitude,
            location.longitude,
            useCelsius
        )

        return when {
            result.isSuccessful -> WeatherWithStatus(
                status = Success,
                weatherModel = result.getOrNull(),
                location = location
            )
            else -> WeatherWithStatus(status = Error.ApiError)
        }
    }
}

/**
 * Data class for the current weather with status.
 */
data class WeatherWithStatus(
    val status: Status = Idle,
    val weatherModel: WeatherModel? = null,
    val location: GeoLocation? = null
)
