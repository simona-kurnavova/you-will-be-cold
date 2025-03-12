package com.youllbecold.trustme.common.domain.weather

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.location.GeoLocationProvider
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.model.WeatherModel
import org.koin.core.annotation.Singleton

/**
 * Use case for fetching and refreshing the hourly weather.
 */
@Singleton
class HourlyWeatherProvider(
    private val app: Application,
    private val weatherRepository: WeatherRepository,
    private val networkStatusProvider: NetworkStatusProvider,
    private val geoLocationProvider: GeoLocationProvider,
) {

    /**
     * Fetches the hourly weather for the current location.
     *
     * @param useCelsius Whether to use Celsius units.
     * @param days The number of days to fetch the weather for.
     */
    @SuppressLint("MissingPermission")
    suspend fun fetchHourlyWeather(useCelsius: Boolean, days: Int): HourlyWeatherWithStatus {
        when {
            !networkStatusProvider.hasInternet() ->
                return HourlyWeatherWithStatus(status = LoadingStatus.NoInternet)
            !PermissionChecker.hasLocationPermission(app) ->
                return HourlyWeatherWithStatus(status = LoadingStatus.MissingPermission)
        }

        val location = geoLocationProvider.fetchLocation()
            ?: return HourlyWeatherWithStatus(status = LoadingStatus.GenericError)

        val result = weatherRepository.getHourlyWeather(
            location.latitude,
            location.longitude,
            useCelsius,
            forecastDays = days
        ).getOrNull()

        return HourlyWeatherWithStatus(
            weatherModel = result ?: emptyList(),
            status = if (result != null) LoadingStatus.Success else LoadingStatus.GenericError,
        )
    }
}

data class HourlyWeatherWithStatus(
    val status: LoadingStatus = LoadingStatus.Idle,
    val weatherModel: List<WeatherModel> = emptyList()
)
