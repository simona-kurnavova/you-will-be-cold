package com.youllbecold.trustme.common.domain.usecases.weather

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.usecases.location.FetchLocationUseCase
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.model.Weather
import org.koin.core.annotation.Singleton

/**
 * Use case for fetching and refreshing the current weather.
 */
@Singleton
class CurrentWeatherUseCase(
    private val app: Application,
    private val weatherRepository: WeatherRepository,
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val networkStatusProvider: NetworkStatusProvider
) {
    /**
     * Fetches the current weather for the current location.
     *
     * @param useCelsius Whether to use Celsius units.
     */
    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentWeather(useCelsius: Boolean): WeatherWithStatus {
        // TODO: Extract this check somewhere
        when {
            !networkStatusProvider.hasInternet() ->
                return WeatherWithStatus(status = LoadingStatus.NoInternet)
            !PermissionChecker.hasLocationPermission(app) ->
                return WeatherWithStatus(status = LoadingStatus.MissingPermission)
        }

        val location = fetchLocationUseCase.fetchLocation()
            ?: return WeatherWithStatus(status = LoadingStatus.GenericError)

        val result = weatherRepository.getCurrentWeather(
            location.latitude,
            location.longitude,
            useCelsius
        ).getOrNull()

        return WeatherWithStatus(
            status = if (result != null) LoadingStatus.Success else LoadingStatus.GenericError,
            weather = result,
        )
    }
}

data class WeatherWithStatus(
    val status: LoadingStatus = LoadingStatus.Idle,
    val weather: Weather? = null
)
