package com.youllbecold.weather.internal

import com.youllbecold.weather.internal.request.TemperatureUnitRequest
import com.youllbecold.weather.internal.request.WeatherCategoriesRequest
import com.youllbecold.weather.internal.response.CurrentWeatherResponse
import com.youllbecold.weather.internal.response.PredictedWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Weather API.
 */
internal interface WeatherApi {
    /**
     * Get forecast weather.
     */
    @GET("/v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: String = TemperatureUnitRequest.FAHRENHEIT.value,
        @Query("hourly") categories: List<String> = WeatherCategoriesRequest.entries.map { it.value },
        @Query("forecast_days") forecastDays: Int = 1,
    ): Response<PredictedWeatherResponse>

    /**
     * Get current weather.
     */
    @GET("/v1/forecast")
    suspend fun getCurrent(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: String = TemperatureUnitRequest.FAHRENHEIT.value,
        @Query("current") categories: List<String> = WeatherCategoriesRequest.entries.map { it.value },
    ): Response<CurrentWeatherResponse>
}

