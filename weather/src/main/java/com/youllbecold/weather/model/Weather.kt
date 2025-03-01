package com.youllbecold.weather.model

import java.time.LocalDateTime

/**
 * Weather data.
 */
data class Weather(
    /**
     * Time of the weather prediction.
     */
    val time: LocalDateTime,

    /**
     * Whether the temperature is in Celsius.
     */
    val unitsCelsius: Boolean,

    /**
     * The temperature.
     */
    val temperature: Double,

    /**
     * The apparent temperature.
     */
    val apparentTemperature: Double,

    /**
     * The weather evaluation - specifying simple weather description.
     */
    val weatherEvaluation: WeatherEvaluation,

    /**
     * The relative humidity in percent.
     */
    val relativeHumidity: Int,

    /**
     * The wind speed.
     */
    val windSpeed: Double,

    /**
     * Probability of rain
     */
    val precipitationProbability: Int,

    /**
     * The UV index.
     */
    val uvIndex: Double,
)
