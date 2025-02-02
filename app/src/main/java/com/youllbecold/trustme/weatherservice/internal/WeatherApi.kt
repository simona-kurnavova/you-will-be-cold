package com.youllbecold.trustme.weatherservice.internal

import com.youllbecold.trustme.weatherservice.internal.request.TemperatureUnitRequest
import com.youllbecold.trustme.weatherservice.internal.request.WeatherCategoriesRequest
import com.youllbecold.trustme.weatherservice.internal.response.CurrentWeatherResponse
import com.youllbecold.trustme.weatherservice.internal.response.PredictedWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Weather API.
 */
interface WeatherApi {
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

