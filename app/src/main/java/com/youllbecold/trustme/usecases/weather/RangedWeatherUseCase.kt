package com.youllbecold.trustme.usecases.weather

import android.Manifest
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.utils.Location
import com.youllbecold.trustme.utils.NetworkHelper
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.api.isSuccessful
import com.youllbecold.weather.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import java.time.LocalDate
import java.time.LocalTime

@Singleton
class RangedWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val dataStorePreferences: DataStorePreferences,
    private val networkHelper: NetworkHelper
) {
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun obtainRangedWeather(
        location: Location,
        date: LocalDate,
        timeFrom: LocalTime,
        timeTo: LocalTime
    ): Result<List<Weather>> = withContext(Dispatchers.IO) {
        if (!networkHelper.hasInternet()) {
            return@withContext Result.failure(Exception("No internet connection"))
        }

        val result = weatherRepository.getDatedWeather(
            location.latitude,
            location.longitude,
            dataStorePreferences.useCelsiusUnits.first(),
            date = date,
        )

        return@withContext when {
            result.isSuccessful -> result.getOrNull()
                ?.filter { weather ->
                    val time = weather.time.toLocalTime()
                    time.isAfter(timeFrom) && time.isBefore(timeTo)
                }
                ?.let { Result.success(it) }
                ?: Result.failure(Exception("No weather data"))

            else -> Result.failure(Exception("Error fetching weather data"))
        }
    }
}
