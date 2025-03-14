package com.youllbecold.trustme.log.domain

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.weather.RangedWeatherProvider
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.trustme.log.ui.model.WeatherParams

/**
 * Use case to obtain weather data for a log.
 */
class LogWeatherProvider(
    private val app: Application,
    private val rangedWeatherProvider: RangedWeatherProvider,
    private val locationController: LocationController
) {
    /**
     * Updates the weather data for a log, no error handling, we don't care.
     */
    @SuppressLint("MissingPermission") // It is checked
    suspend fun obtainWeather(dateTimeState: DateTimeState): WeatherParamsWithStatus {
        if (!PermissionChecker.hasLocationPermission(app)) {
            return WeatherParamsWithStatus(Error.MissingPermission)
        }

        val location = locationController.quickGetLastLocation()
            ?: return WeatherParamsWithStatus(Error.LocationMissing)


        val result = rangedWeatherProvider.obtainRangedWeather(
            location = location,
            date = dateTimeState.date.localDate,
            timeFrom = dateTimeState.timeFrom.localTime,
            timeTo = dateTimeState.timeTo.localTime,
            useCelsiusUnits = true
        )

        if (result.status != Success) {
            return WeatherParamsWithStatus(status = result.status)
        }

        val rangedWeather = result.weather
        return WeatherParamsWithStatus(
            status = Success,
            weatherParams = WeatherParams(
                apparentTemperatureMin = rangedWeather.minOf { it.apparentTemperature },
                apparentTemperatureMax = rangedWeather.maxOf { it.apparentTemperature },
                avgTemperature = rangedWeather.sumOf { it.temperature } / rangedWeather.size.toDouble(),
                useCelsiusUnits = true
            )
        )
    }
}

data class WeatherParamsWithStatus(
    val status: Status = Idle,
    val weatherParams: WeatherParams? = null
)
