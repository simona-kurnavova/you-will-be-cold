package com.youllbecold.trustme.usecases.weather

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.ui.viewmodels.WeatherState
import com.youllbecold.trustme.utils.GeoLocation
import com.youllbecold.trustme.utils.NetworkHelper
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.api.isSuccessful
import java.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import java.time.LocalDate
import java.time.LocalTime

/**
 * Use case for fetching the weather for a specific time range.
 */
@Singleton
class RangedWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val networkHelper: NetworkHelper
) {
    /**
     * Obtains the weather for a specific time range.
     *
     * @param location The location to obtain the weather for.
     * @param date The date to obtain the weather for.
     * @param timeFrom The start time of the range.
     * @param timeTo The end time of the range.
     * @param useCelsiusUnits Whether to use Celsius units.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun obtainRangedWeather(
        location: GeoLocation,
        date: LocalDate,
        timeFrom: LocalTime,
        timeTo: LocalTime,
        useCelsiusUnits: Boolean = true
    ): Result<WeatherState> = withContext(Dispatchers.IO) {
        if (!networkHelper.hasInternet()) {
            return@withContext Result.failure(Exception("No internet connection"))
        }

        val result = weatherRepository.getDatedWeather(
            location.latitude,
            location.longitude,
            useCelsiusUnits,
            date = date,
        )

        return@withContext when {
            result.isSuccessful -> {
                val data = result.getOrNull()

                if (data.isNullOrEmpty()) {
                    return@withContext Result.failure(Exception("No weather data"))
                }

                val adjustedTimeRange = adjustTimeRange(timeFrom, timeTo)

                val range = data.filter { weather ->
                    val time = weather.time.toLocalTime()
                    time.isAfter(adjustedTimeRange.first) && time.isBefore(adjustedTimeRange.second)
                }

                return@withContext WeatherState(
                    apparentTemperatureMin = range.minOf { it.apparentTemperature },
                    apparentTemperatureMax = range.maxOf { it.apparentTemperature },
                    avgTemperature = range.sumOf { it.temperature } / range.size.toDouble()
                ).let { Result.success(it) }
            }

            else -> Result.failure(Exception("Error fetching weather data"))
        }
    }

    private fun adjustTimeRange(timeFrom: LocalTime, timeTo: LocalTime): Pair<LocalTime, LocalTime> {
        val duration = Duration.between(timeFrom, timeTo)

        return if (duration.toMinutes() <= MINUTES_IN_HOUR) {
            val expandedTimeFrom = timeFrom.minusMinutes(30)  // 30 minutes before
            val expandedTimeTo = timeTo.plusMinutes(30)      // 30 minutes after
            Pair(expandedTimeFrom, expandedTimeTo)
        } else {
            Pair(timeFrom, timeTo)
        }
    }
}

private const val MINUTES_IN_HOUR = 60
