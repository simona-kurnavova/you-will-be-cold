package com.youllbecold.trustme.usecases.weather

import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.usecases.weather.state.ErrorType
import com.youllbecold.trustme.usecases.weather.state.WeatherState
import com.youllbecold.trustme.usecases.weather.state.WeatherUseCaseStatus
import com.youllbecold.trustme.usecases.weather.state.copyWithError
import com.youllbecold.trustme.usecases.weather.state.copyWithLoading
import com.youllbecold.trustme.usecases.weather.state.copyWithNetworkResult
import com.youllbecold.trustme.utils.GeoLocation
import com.youllbecold.trustme.utils.NetworkHelper
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
    private val weatherRepository: WeatherRepository,
    private val dataStorePreferences: DataStorePreferences,
    private val networkHelper: NetworkHelper
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var currentLocation: GeoLocation? = null

    private val _weatherState: MutableStateFlow<WeatherState<Weather?>> =
        MutableStateFlow(
            WeatherState(
                weather = null,
                status = WeatherUseCaseStatus.Idle,
            )
        )

    /**
     * State flow for the current weather.
     */
    val weatherState: StateFlow<WeatherState<Weather?>> = _weatherState

    /**
     * Refreshes the current weather for the given location.
     */
    fun refreshCurrentWeather(location: GeoLocation) {
        currentLocation = location
        _weatherState.update { it.copyWithLoading()  }

        if (!networkHelper.hasInternet()) {
            _weatherState.update { it.copyWithError(ErrorType.OFFLINE)  }
            return
        }

        coroutineScope.launch {
            val result = weatherRepository.getCurrentWeather(
                location.latitude,
                location.longitude,
                dataStorePreferences.useCelsiusUnits.first()
            )

            _weatherState.update { it.copyWithNetworkResult(result) }
        }
    }

    /**
     * Called when the units are changed, to quick refresh the current weather.
     */
    fun onUnitsChanged() {
        currentLocation?.let { refreshCurrentWeather(it) }
    }

    /**
     * Fetches the current weather for the given location, no error handling, just result or null.
     */
    suspend fun getCurrentWeather(location: GeoLocation): Weather? = with(Dispatchers.IO) {
        val result = weatherRepository.getCurrentWeather(
            location.latitude,
            location.longitude,
            dataStorePreferences.useCelsiusUnits.first()
        )
        return result.getOrNull()
    }
}
