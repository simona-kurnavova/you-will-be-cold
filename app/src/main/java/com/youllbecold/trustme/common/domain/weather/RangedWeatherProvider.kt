package com.youllbecold.trustme.common.domain.weather

import android.Manifest
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.api.isSuccessful
import com.youllbecold.weather.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

/**
 * Provider for fetching the weather for a specific time range.
 */
class RangedWeatherProvider(
    private val weatherRepository: WeatherRepository,
    private val networkStatusProvider: NetworkStatusProvider
) {
    private val dispatchers = Dispatchers.Default

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
    ): RangedWeatherWithStatus = withContext(dispatchers) {
        if (!networkStatusProvider.hasInternet()) {
            return@withContext RangedWeatherWithStatus(status = Error.NoInternet)
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
                    return@withContext RangedWeatherWithStatus(status = Error.ApiError)
                }

                // In case the difference is smaller than hour, expand range as API cannot provide less than hourly.
                val adjustedTimeRange = adjustTimeRange(timeFrom, timeTo)

                return@withContext data.filter { weather ->
                    val time = weather.time.millisToDateTime.toLocalTime()
                    time.isAfter(adjustedTimeRange.first) && time.isBefore(adjustedTimeRange.second) // Sanity check (not mine, the code's)
                }.let {
                    RangedWeatherWithStatus(status = Success, weather = it)
                }
            }

            else -> RangedWeatherWithStatus(status = Error.ApiError)
        }
    }

    private suspend fun adjustTimeRange(
        timeFrom: LocalTime,
        timeTo: LocalTime
    ): Pair<LocalTime, LocalTime> = withContext(dispatchers) { // Calculations
        val duration = Duration.between(timeFrom, timeTo)

        if (duration.toMinutes() <= MINUTES_IN_HOUR) {
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

/**
 * Data class for the ranged weather with status.
 */
data class RangedWeatherWithStatus(
    val status: Status = Idle,
    val weather: List<WeatherModel> = emptyList()
)
