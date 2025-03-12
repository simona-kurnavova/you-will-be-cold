package com.youllbecold.weather.api

import com.youllbecold.weather.model.WeatherModel
import java.time.LocalDate

/**
 * Repository for weather data.
 */
interface WeatherRepository {
    /**
     * Get current weather.
     */
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        useCelsius: Boolean,
    ): Result<WeatherModel>

    /**
     * Get hourly weather for specified number of days.
     * Note: The data include also past hours of today.
     */
    suspend fun getHourlyWeather(
        latitude: Double,
        longitude: Double,
        useCelsius: Boolean,
        forecastDays: Int = 1
    ): Result<List<WeatherModel>>

    /**
     * Get forecast weather for a specific date.
     */
    suspend fun getDatedWeather(
        latitude: Double,
        longitude: Double,
        useCelsius: Boolean,
        date: LocalDate,
    ): Result<List<WeatherModel>>
}

val <T> Result<T>.isSuccessful: Boolean
    get() = this.isSuccess && this.getOrNull() != null
