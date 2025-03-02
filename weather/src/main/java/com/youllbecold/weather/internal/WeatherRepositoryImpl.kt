package com.youllbecold.weather.internal

import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.internal.request.TemperatureUnitRequest
import com.youllbecold.weather.internal.response.CurrentWeatherResponse
import com.youllbecold.weather.internal.response.TemperatureUnit
import com.youllbecold.weather.internal.response.PredictedWeatherResponse
import com.youllbecold.weather.model.WeatherEvaluation
import com.youllbecold.weather.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Repository for weather data.
 */
internal class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi
) : WeatherRepository {
    private val dispatchers = Dispatchers.IO

    /**
     * Get current weather.
     */
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, useCelsius: Boolean): Result<Weather> =
        processCall(
            call = { weatherApi.getCurrentWeather(latitude, longitude, temperatureUnit = getUnits(useCelsius)) },
            processBody = { responseBody -> responseBody.toWeather() }
        )

    override suspend fun getHourlyWeather(latitude: Double, longitude: Double, useCelsius: Boolean, forecastDays: Int): Result<List<Weather>> =
        processCall(
            call = { weatherApi.getHourlyWeather(latitude, longitude, temperatureUnit = getUnits(useCelsius), forecastDays = forecastDays) },
            processBody = { responseBody -> responseBody.toWeatherList() }
        )

    override suspend fun getDatedWeather(
        latitude: Double,
        longitude: Double,
        useCelsius: Boolean,
        date: LocalDate,
    ): Result<List<Weather>> {
        val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

        return processCall(
            call = { weatherApi.getHourlyWeatherForDateRange(
                latitude,
                longitude,
                temperatureUnit = getUnits(useCelsius),
                startDate = dateString,
                endDate = dateString
            ) },
            processBody = { responseBody -> responseBody.toWeatherList() }
        )
    }

    private fun getUnits(useCelsius: Boolean): String =
        if (useCelsius) TemperatureUnitRequest.CELSIUS.value else TemperatureUnitRequest.FAHRENHEIT.value

    private suspend fun <R, T> processCall(
        call: suspend () -> Response<R>,
        processBody: (R) -> T
    ): Result<T> = withContext(dispatchers) {
        try {
            call().processResponse(processBody)
        } catch (e: IOException) { // Network issues
            Result.failure(Exception("No internet connection. Please try again later."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun <R, T> Response<R>.processResponse(
        processBody: (R) -> T
    ): Result<T> = if (isSuccessful) {
        val body = body()

        if (body == null) {
            Result.failure(Exception("Weather data is null"))
        } else {
            Result.success(processBody(body))
        }
    } else {
        Result.failure(
            Exception("Error while fetching, code: ${code()}, message: ${message()}")
        )
    }

    private fun CurrentWeatherResponse.toWeather(): Weather = Weather(
        time = LocalDateTime.parse(current.time).toMillis(),
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

    private fun PredictedWeatherResponse.toWeatherList(): List<Weather> {
        val unitsCelsius = hourlyUnits.temperatureUnit == TemperatureUnit.CELSIUS

        return hourly.time.mapIndexed { index, time ->
            Weather(
                time = LocalDateTime.parse(time).toMillis(),
                unitsCelsius = unitsCelsius,
                temperature = hourly.temperature2m[index],
                apparentTemperature = hourly.apparentTemperature[index],
                weatherEvaluation = hourly.weatherCode[index].toEvaluation(),
                relativeHumidity = hourly.relativeHumidity[index],
                windSpeed = hourly.windSpeed[index],
                precipitationProbability = hourly.precipitationProbability[index],
                uvIndex = hourly.uvIndex[index]
            )
        }
    }

    private fun LocalDateTime.toMillis(): Long =
        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
