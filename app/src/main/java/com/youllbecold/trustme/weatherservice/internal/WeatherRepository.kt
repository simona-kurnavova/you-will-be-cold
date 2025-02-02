package com.youllbecold.trustme.weatherservice.internal

import com.youllbecold.trustme.utils.Location
import com.youllbecold.trustme.weatherservice.internal.request.TemperatureUnitRequest
import com.youllbecold.trustme.weatherservice.internal.response.CurrentWeatherResponse
import com.youllbecold.trustme.weatherservice.internal.response.Hourly
import com.youllbecold.trustme.weatherservice.internal.response.TemperatureUnit
import com.youllbecold.trustme.weatherservice.internal.response.PredictedWeatherResponse
import com.youllbecold.trustme.weatherservice.model.HourlyData
import com.youllbecold.trustme.weatherservice.model.WeatherEvaluation
import com.youllbecold.trustme.weatherservice.model.WeatherNow
import com.youllbecold.trustme.weatherservice.model.WeatherPrediction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDateTime

/**
 * Repository for weather data.
 */
class WeatherRepository(
    private val weatherDao: WeatherApi
) {
    private val _prediction: MutableStateFlow<WeatherPrediction?> = MutableStateFlow(null)
    private val _current: MutableStateFlow<WeatherNow?> = MutableStateFlow(null)

    /**
     * Current weather.
     */
    val current: StateFlow<WeatherNow?> = _current

    /**
     * Prediction weather for today.
     */
    val prediction: StateFlow<WeatherPrediction?> = _prediction

    /**
     * Get current weather.
     */
    suspend fun getCurrent(location: Location, useCelsius: Boolean): Result<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext weatherDao.getCurrent(location.latitude, location.longitude, temperatureUnit = getUnits(useCelsius))
                .processResponse { responseBody ->
                    _current.value = responseBody.toWeather(location.city)
                }
        }

    /**
     * Get forecast weather for today.
     */
    suspend fun getForecast(location: Location, useCelsius: Boolean): Result<Unit> =
        withContext(Dispatchers.IO) {
            weatherDao.getForecast(location.latitude, location.longitude, temperatureUnit = getUnits(useCelsius))
                .processResponse { responseBody ->
                    _prediction.value = responseBody.toWeather(location.city)
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

    private fun PredictedWeatherResponse.toWeather(city: String?): WeatherPrediction = WeatherPrediction(
        city = city,
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

    private fun CurrentWeatherResponse.toWeather(city: String?): WeatherNow = WeatherNow(
        city = city,
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
