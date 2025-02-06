package com.youllbecold.weather.api

import com.youllbecold.weather.model.WeatherNow
import com.youllbecold.weather.model.WeatherPrediction
import kotlinx.coroutines.flow.StateFlow

interface WeatherRepository {
    val currentWeather: StateFlow<WeatherNow?>

    val futureWeather: StateFlow<WeatherPrediction?>

    suspend fun getCurrentWeather(latitude: Double, longitude: Double, useCelsius: Boolean): Result<Unit>

    suspend fun getFutureWeather(latitude: Double, longitude: Double, useCelsius: Boolean, days: Int): Result<Unit>
}