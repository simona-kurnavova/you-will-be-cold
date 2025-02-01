package com.youllbecold.trustme.weatherservice.internal

import com.youllbecold.trustme.model.TemperatureUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

/**
 * Repository for weather data.
 */
class WeatherRepository(
    private val weatherDao: WeatherApi
) {
    private val _currentWeather: MutableStateFlow<WeatherStats?> = MutableStateFlow(null)

    /**
     * Current weather.
     */
    val currentWeather: StateFlow<WeatherStats?> = _currentWeather

    suspend fun getForecast(latitude: Float, longitude: Float): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = weatherDao.getForecast(latitude, longitude)

            return@withContext if (response.isSuccessful) {
                val body = response.body()
                _currentWeather.value = body?.toWeather()

                if (body == null) {
                    Result.failure<Unit>(Exception("Weather data is null"))
                } else {
                    Result.success(Unit)
                }
            } else {
                Result.failure<Unit>(
                    Exception("Error while fetching, code: ${response.code()}, message: ${response.message()}")
                )
            }
        }

    private fun WeatherResponse.toWeather(): WeatherStats = WeatherStats(
        latitude = latitude,
        longitude = longitude,
        elevation = elevation,
        timezone = timezone,
        timezoneAbbreviation = timezoneAbbreviation,
        temperatureUnit = TemperatureUnit.CELSIUS,
        temperatures = Temperatures(
            hourly?.time.orEmpty(),
            hourly?.temperature2m.orEmpty(),
        )
    )
}

data class WeatherStats(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    val timezone: String,
    val timezoneAbbreviation: String,
    val temperatureUnit: TemperatureUnit,
    val temperatures: Temperatures
)

data class Temperatures(
    val time: List<String>,
    val temperatures: List<Double>
)

private const val UNAUTHORIZED_CODE = 401
private const val NOT_FOUND_CODE = 404
private const val INTERNAL_SERVER_ERROR_CODE = 500
