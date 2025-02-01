package com.youllbecold.trustme.weatherservice.internal

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("hourly") categories: List<String> = WeatherCategories.entries.map { it.value },
        @Query("forecast_days") forecastDays: Int = 1,
    ): Response<WeatherResponse>
}

enum class WeatherCategories(val value: String = "") {
    TEMPERATURE("temperature_2m"),
    RAIN("rain"),
    WIND_SPEED("wind_speed_10m"),
    UV_INDEX("uv_index")
}
