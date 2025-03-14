package com.youllbecold.weather.internal.data.repository

import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.api.WeatherResult
import com.youllbecold.weather.internal.data.dao.WeatherDao
import com.youllbecold.weather.internal.data.mappers.toWeather
import com.youllbecold.weather.internal.data.mappers.toWeatherList
import com.youllbecold.weather.internal.data.mappers.toWeatherResult
import com.youllbecold.weather.internal.data.request.TemperatureUnitRequest
import com.youllbecold.weather.model.WeatherModel
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Repository for weather data.
 */
internal class WeatherRepositoryImpl(
    private val weatherDao: WeatherDao
) : WeatherRepository {

    /**
     * Get current weather.
     */
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, useCelsius: Boolean): WeatherResult<WeatherModel> =
        processCall(
            call = { weatherDao.getCurrentWeather(latitude, longitude, temperatureUnit = getUnits(useCelsius)) },
            processBody = { responseBody -> responseBody.toWeather() }
        )

    override suspend fun getHourlyWeather(latitude: Double, longitude: Double, useCelsius: Boolean, forecastDays: Int): WeatherResult<List<WeatherModel>> =
        processCall(
            call = { weatherDao.getHourlyWeather(latitude, longitude, temperatureUnit = getUnits(useCelsius), forecastDays = forecastDays) },
            processBody = { responseBody -> responseBody.toWeatherList() }
        )

    override suspend fun getDatedWeather(
        latitude: Double,
        longitude: Double,
        useCelsius: Boolean,
        date: LocalDate,
    ): WeatherResult<List<WeatherModel>> {
        val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

        return processCall(
            call = { weatherDao.getHourlyWeatherForDateRange(
                latitude,
                longitude,
                temperatureUnit = getUnits(useCelsius),
                startDate = dateString,
                endDate = dateString
            ) },
            processBody = { responseBody -> responseBody.toWeatherList() }
        )
    }

    private suspend fun <R, T> processCall(
        call: suspend () -> Response<R>,
        processBody: (R) -> T,
    ): WeatherResult<T> = try {
        val result = call()
        result.toWeatherResult(processBody)
    } catch (_: IOException) { // Network issues
        WeatherResult.Error.NoInternet
    } catch (e: Exception) {
        WeatherResult.Error.UnknownError
    }

    private fun getUnits(useCelsius: Boolean): String =
        if (useCelsius) TemperatureUnitRequest.CELSIUS.value else TemperatureUnitRequest.FAHRENHEIT.value
}
