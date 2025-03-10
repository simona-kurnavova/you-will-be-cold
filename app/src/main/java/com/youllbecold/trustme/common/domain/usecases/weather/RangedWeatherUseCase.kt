package com.youllbecold.trustme.common.domain.usecases.weather

import android.Manifest
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.common.ui.model.log.WeatherParams
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.api.isSuccessful
import com.youllbecold.weather.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

/**
 * Use case for fetching the weather for a specific time range.
 */
@Singleton
class RangedWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val networkStatusProvider: NetworkStatusProvider
) {
    private val dispatchers = Dispatchers.IO

    /**
     * Obtains the weather for a specific time range. Returns simplified weather data.
     *
     * @param location The location to obtain the weather for.
     * @param date The date to obtain the weather for.
     * @param timeFrom The start time of the range.
     * @param timeTo The end time of the range.
     * @param useCelsiusUnits Whether to use Celsius units.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun obtainRangedWeatherState(
        location: GeoLocation,
        date: LocalDate,
        timeFrom: LocalTime,
        timeTo: LocalTime,
        useCelsiusUnits: Boolean
    ): Result<WeatherParams> = withContext(dispatchers) {
        val weatherList = obtainRangedWeather(location, date, timeFrom, timeTo, useCelsiusUnits)

        return@withContext weatherList.map { rangedWeather ->
            WeatherParams(
                apparentTemperatureMin = rangedWeather.minOf { it.apparentTemperature },
                apparentTemperatureMax = rangedWeather.maxOf { it.apparentTemperature },
                avgTemperature = rangedWeather.sumOf { it.temperature } / rangedWeather.size.toDouble(),
                useCelsiusUnits = useCelsiusUnits
            )
        }
    }

    /**
     * Obtains the weather for a specific time range. Returns a list of weather data.
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
        useCelsiusUnits: Boolean
    ): Result<List<Weather>> = withContext(dispatchers) {
        if (!networkStatusProvider.hasInternet()) {
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

                // In case the difference is smaller than hour, expand range as API cannot provide less than hourly.
                val adjustedTimeRange = adjustTimeRange(timeFrom, timeTo)

                return@withContext data.filter { weather ->
                    val time = weather.time.millisToDateTime.toLocalTime()
                    time.isAfter(adjustedTimeRange.first) && time.isBefore(adjustedTimeRange.second)
                }.let { Result.success(it) }
            }

            else -> Result.failure(Exception("Error fetching weather data"))
        }
    }

    private fun adjustTimeRange(timeFrom: LocalTime, timeTo: LocalTime): Pair<LocalTime, LocalTime> {
        val duration = Duration.between(timeFrom, timeTo)

        return if (duration.toMinutes() <= MINUTES_IN_HOUR) {
            val expandedTimeFrom = timeFrom.minusMinutes(MINUTES_IN_HALF_HOUR)  // 30 minutes before
            val expandedTimeTo = timeTo.plusMinutes(MINUTES_IN_HALF_HOUR)      // 30 minutes after
            Pair(expandedTimeFrom, expandedTimeTo)
        } else {
            Pair(timeFrom, timeTo)
        }
    }
}

private const val MINUTES_IN_HOUR = 60
private const val MINUTES_IN_HALF_HOUR: Long = 30
