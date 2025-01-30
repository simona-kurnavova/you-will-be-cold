package com.youllbecold.trustme.weatherservice

import android.util.Log
import com.youllbecold.trustme.model.Temperatures
import com.youllbecold.trustme.model.WeatherStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherDao: WeatherApi
) {
    private val dispatchers = Dispatchers.IO

    private val _currentWeather: MutableStateFlow<WeatherStats?> = MutableStateFlow(null)

    val currentWeather: StateFlow<WeatherStats?> = _currentWeather

    suspend fun getForecast(latitude: Float, longitude: Float): Result<Unit> =
        withContext(dispatchers) {
            val response = weatherDao.getForecast(latitude, longitude)
            Log.d("WeatherRepository", "getForecast: $response")
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                    ?: return@withContext Result.failure<Unit>(Exception("Empty response"))
                _currentWeather.value = weatherResponse.toWeatherStats()
                return@withContext Result.success(Unit)
            }
            return@withContext Result.failure<Unit>(Exception("Failed to fetch weather data"))
        }

    private fun WeatherResponse.toWeatherStats(): WeatherStats = WeatherStats(
        latitude = latitude,
        longitude = longitude,
        elevation = elevation,
        timezone = timezone,
        timezoneAbbreviation = timezoneAbbreviation,
        temperatures = Temperatures(
            hourly.time,
            hourly.temperature2m
        )
    )
}