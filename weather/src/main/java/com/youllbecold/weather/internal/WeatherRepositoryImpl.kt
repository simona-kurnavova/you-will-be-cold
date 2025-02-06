package com.youllbecold.weather.internal

import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.internal.request.TemperatureUnitRequest
import com.youllbecold.weather.internal.response.CurrentWeatherResponse
import com.youllbecold.weather.internal.response.Hourly
import com.youllbecold.weather.internal.response.TemperatureUnit
import com.youllbecold.weather.internal.response.PredictedWeatherResponse
import com.youllbecold.weather.model.HourlyData
import com.youllbecold.weather.model.WeatherEvaluation
import com.youllbecold.weather.model.WeatherNow
import com.youllbecold.weather.model.WeatherPrediction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDateTime

/**
 * Repository for weather data.
 */
internal class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi
) : WeatherRepository {
    private val dispatchers = Dispatchers.IO

    private val _futureWeather: MutableStateFlow<WeatherPrediction?> = MutableStateFlow(null)
    private val _currentWeather: MutableStateFlow<WeatherNow?> = MutableStateFlow(null)

    override val currentWeather: StateFlow<WeatherNow?>
        get() = _currentWeather.asStateFlow()

    override val futureWeather: StateFlow<WeatherPrediction?>
        get() = _futureWeather.asStateFlow()

    /**
     * Get current weather.
     */
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, useCelsius: Boolean): Result<Unit> =
        withContext(dispatchers) {
            weatherApi.getCurrent(latitude, longitude, temperatureUnit = getUnits(useCelsius))
                .processResponse { responseBody ->
                    _currentWeather.value = responseBody.toWeather()
                }
        }

    /**
     * Get forecast weather for today.
     */
    override suspend fun getFutureWeather(latitude: Double, longitude: Double, useCelsius: Boolean, days: Int): Result<Unit> =
        withContext(dispatchers) {
            weatherApi.getForecast(latitude, longitude, temperatureUnit = getUnits(useCelsius), forecastDays = days)
                .processResponse { responseBody ->
                    _futureWeather.value = responseBody.toWeather()
                }
        }

    private fun getUnits(useCelsius: Boolean): String =
        if (useCelsius) TemperatureUnitRequest.CELSIUS.value else TemperatureUnitRequest.FAHRENHEIT.value

    private fun <R, T> Response<R>.processResponse(
        processBody: (R) -> T
    ): Result<Unit> = if (isSuccessful) {
        val body = body()

        if (body == null) {
            Result.failure(Exception("Weather data is null"))
        } else {
            processBody(body)
            Result.success(Unit)
        }
    } else {
        Result.failure(
            Exception("Error while fetching, code: ${code()}, message: ${message()}")
        )
    }

    private fun PredictedWeatherResponse.toWeather(): WeatherPrediction = WeatherPrediction(
        unitsCelsius = hourlyUnits.temperatureUnit == TemperatureUnit.CELSIUS,
        hourlyData = hourly.toHourlyData()
    )

    private fun Hourly.toHourlyData(): List<HourlyData> {
        val hourlyData = mutableListOf<HourlyData>()
        time.forEachIndexed { index: Int, time: String ->
           val dateTime = LocalDateTime.parse(time)
            hourlyData.add(
                HourlyData(
                    time = dateTime,
                    temperature = temperature2m[index],
                    apparentTemperature = apparentTemperature[index],
                    weatherEvaluation = weatherCode[index].toEvaluation(),
                    relativeHumidity = relativeHumidity[index],
                    windSpeed = windSpeed[index],
                    precipitationProbability = precipitationProbability[index],
                    uvIndex = uvIndex[index]
                )
            )
        }
        return hourlyData
    }

    private fun CurrentWeatherResponse.toWeather(): WeatherNow = WeatherNow(
        unitsCelsius = units.temperatureUnit == TemperatureUnit.CELSIUS,
        temperature = current.temperature2m,
        apparentTemperature = current.apparentTemperature,
        relativeHumidity = current.relativeHumidity,
        windSpeed = current.windSpeed,
        precipitationProbability = current.precipitationProbability,
        uvIndex = current.uvIndex,
        weatherEvaluation = current.weatherCode.toEvaluation()
    )

    // For details see table: https://open-meteo.com/en/docs#latitude=52.52&longitude=13.41&current=&minutely_15=&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability,rain,snowfall,wind_speed_10m,uv_index&daily=&location_mode=csv_coordinates&forecast_days=1&models=
    private fun Int.toEvaluation(): WeatherEvaluation =
        when(this) {
            0 -> WeatherEvaluation.SUNNY
            1, 2, 3 -> WeatherEvaluation.CLOUDY
            45, 48 -> WeatherEvaluation.FOGGY
            51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> WeatherEvaluation.RAINY
            71, 73, 75, 77, 85, 86 -> WeatherEvaluation.SNOWY
            95, 96, 99 -> WeatherEvaluation.STORM
            else -> WeatherEvaluation.UNKNOWN
        }
}
