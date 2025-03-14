package com.youllbecold.trustme.common.domain.weather

import android.annotation.SuppressLint
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.domain.weather.utils.WeatherPrerequisitesChecker
import com.youllbecold.trustme.common.ui.mappers.toError
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.api.WeatherResult
import com.youllbecold.weather.model.WeatherModel
import org.koin.core.annotation.Singleton

/**
 * Provider for fetching and refreshing the hourly weather and mapping to UI-ready Status.
 */
@Singleton
class HourlyWeatherProvider(
    private val weatherRepository: WeatherRepository,
    private val locationController: LocationController,
) {

    /**
     * Fetches the hourly weather for the current location.
     *
     * @param useCelsius Whether to use Celsius units.
     * @param days The number of days to fetch the weather for.
     */
    @SuppressLint("MissingPermission") // Checked in prerequisites
    suspend fun fetchHourlyWeather(useCelsius: Boolean, days: Int): HourlyWeatherWithStatus {
        WeatherPrerequisitesChecker.check().let {
            if (it != Success) {
                return HourlyWeatherWithStatus(status = it)
            }
        }

        val location = locationController.quickGetLastLocation()
            ?: return HourlyWeatherWithStatus(status = Error.LocationMissing)

        val result = weatherRepository.getHourlyWeather(
            location.latitude,
            location.longitude,
            useCelsius,
            forecastDays = days
        )

        return when(result) {
            is WeatherResult.Success -> HourlyWeatherWithStatus(
                status = Success,
                weatherModel = result.data
            )
            is WeatherResult.Error ->
                HourlyWeatherWithStatus(status =  result.toError())
        }
    }
}

/**
 * Data class for the hourly weather with status.
 */
data class HourlyWeatherWithStatus(
    val status: Status = Idle,
    val weatherModel: List<WeatherModel> = emptyList()
)
