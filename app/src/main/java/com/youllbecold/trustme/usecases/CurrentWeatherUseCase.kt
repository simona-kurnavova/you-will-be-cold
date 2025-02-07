package com.youllbecold.trustme.usecases

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.utils.Location
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.model.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

/**
 * Use case for fetching and refreshing the current weather.
 */
@Singleton
class CurrentWeatherUseCase(
    private val app: Application,
    private val weatherRepository: WeatherRepository,
    private val dataStorePreferences: DataStorePreferences,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _weatherState: MutableStateFlow<CurrentWeatherState> = MutableStateFlow(CurrentWeatherState(status = CurrentWeatherStatus.Idle))
    val weatherState: StateFlow<CurrentWeatherState> = _weatherState

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun refreshCurrentWeather() {
        _weatherState.update { it.copy(status = CurrentWeatherStatus.Loading) }

        LocationHelper.refreshLocation(
            app,
            onSuccess = ::onLocationObtained,
            onError = {
                _weatherState.update { it.copy(status = CurrentWeatherStatus.Error) }
            }
        )
    }

    private fun onLocationObtained(location: Location) {
        coroutineScope.launch {
            val useCelsius = dataStorePreferences.useCelsiusUnits.first()
            val result = weatherRepository.getCurrentWeather(location.latitude, location.longitude, useCelsius)

            val weather = result.getOrNull()

            when {
                result.isSuccess && weather != null -> _weatherState.update {
                    CurrentWeatherState(
                        status = CurrentWeatherStatus.Success,
                        weather = weather,
                        city = location.city
                    )
                }

                else -> _weatherState.update { it.copy(status = CurrentWeatherStatus.Error) }
            }
        }
    }
}

data class CurrentWeatherState(
    val status: CurrentWeatherStatus,
    val weather: Weather? = null,
    val city: String? = null,
)

enum class CurrentWeatherStatus {
    Idle,
    Loading,
    Error,
    Success
}
