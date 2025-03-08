package com.youllbecold.weather.internal.data.request

/**
 * Enum class that represents the different weather categories that can be requested.
 */
internal enum class WeatherCategoriesRequest(val value: String) {
    TEMPERATURE("temperature_2m"),
    RAIN("rain"),
    WIND_SPEED("wind_speed_10m"),
    UV_INDEX("uv_index"),
    WEATHER_CODE("weather_code"),
    RELATIVE_HUMIDITY("relative_humidity_2m"),
    PRECIPITATION_PROBABILITY("precipitation_probability"),
    APPARENT_TEMPERATURE("apparent_temperature"),
}
