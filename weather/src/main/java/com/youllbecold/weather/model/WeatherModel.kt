package com.youllbecold.weather.model

/**
 * Weather data.
 *
 * @property time Time of the weather prediction.
 * @property unitsCelsius Whether the temperature is in Celsius.
 * @property temperature The temperature.
 * @property apparentTemperature The apparent temperature.
 * @property weatherEvaluation The weather evaluation - specifying simple weather description.
 * @property relativeHumidity The relative humidity in percent.
 * @property windSpeed The wind speed.
 * @property precipitationProbability Probability of rain
 * @property uvIndex The UV index.
 */
data class WeatherModel(
    val time: Long,
    val unitsCelsius: Boolean,
    val temperature: Double,
    val apparentTemperature: Double,
    val weatherEvaluation: WeatherEvaluation,
    val relativeHumidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int,
    val uvIndex: Double,
)
