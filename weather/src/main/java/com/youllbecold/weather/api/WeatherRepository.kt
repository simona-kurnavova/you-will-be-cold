package com.youllbecold.weather.api

import com.youllbecold.weather.model.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        useCelsius: Boolean
    ): Result<Weather>

    suspend fun getHourlyWeather(
        latitude: Double,
        longitude: Double,
        useCelsius: Boolean
    ): Result<List<Weather>>
}

val <T> Result<T>.isSuccessful: Boolean
    get() = this.isSuccess && this.getOrNull() != null
