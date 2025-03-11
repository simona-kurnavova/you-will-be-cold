package com.youllbecold.trustme.log.usecases

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.domain.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.log.ui.model.WeatherParams
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalTime

/**
 * Use case to obtain weather data for a log.
 */
class ObtainLogWeatherParamsUseCase(
    private val app: Application,
    private val rangedWeatherUseCase: RangedWeatherUseCase,
    private val locationController: LocationController
) {
    /**
     * Updates the weather data for a log.
     */
    @SuppressLint("MissingPermission") // It is checked
    suspend fun obtainWeather(dateTimeState: DateTimeState): WeatherParams? {
        val location = locationController.geoLocationState.firstOrNull()?.location
            ?: LocationController.getLastLocation(app)

        if (location == null) {
            return null
        }

        val weather = obtainRangedWeatherState(
            location = location,
            date = dateTimeState.date.localDate,
            timeFrom = dateTimeState.timeFrom.localTime,
            timeTo = dateTimeState.timeTo.localTime,
            useCelsiusUnits = true
        ).getOrNull()

        return weather
    }

    /**
     * Obtains the weather for a specific time range. Returns simplified weather data.
     */
    @SuppressLint("MissingPermission")
    private suspend fun obtainRangedWeatherState(
        location: GeoLocation,
        date: LocalDate,
        timeFrom: LocalTime,
        timeTo: LocalTime,
        useCelsiusUnits: Boolean
    ): Result<WeatherParams> {
        val weatherList = rangedWeatherUseCase.obtainRangedWeather(location, date, timeFrom, timeTo, useCelsiusUnits)

        return weatherList.map { rangedWeather ->
            WeatherParams(
                apparentTemperatureMin = rangedWeather.minOf { it.apparentTemperature },
                apparentTemperatureMax = rangedWeather.maxOf { it.apparentTemperature },
                avgTemperature = rangedWeather.sumOf { it.temperature } / rangedWeather.size.toDouble(),
                useCelsiusUnits = useCelsiusUnits
            )
        }
    }
}