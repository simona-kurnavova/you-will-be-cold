package com.youllbecold.weather.internal.data.mappers

import com.youllbecold.weather.internal.data.response.CurrentWeatherResponse
import com.youllbecold.weather.internal.data.response.PredictedWeatherResponse
import com.youllbecold.weather.internal.data.response.TemperatureUnit
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Maps [PredictedWeatherResponse] to a list of [Weather].
 */
internal fun PredictedWeatherResponse.toWeatherList(): List<Weather> {
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

/**
 * Maps [CurrentWeatherResponse] to [Weather].
 */
internal fun CurrentWeatherResponse.toWeather(): Weather = Weather(
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

private fun LocalDateTime.toMillis(): Long =
    atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()