package com.youllbecold.trustme.usecases.weather

import android.util.Log
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

    private val _weatherState: MutableStateFlow<WeatherState<Weather?>> =
        MutableStateFlow(
            WeatherState(
                weather = null,
                status = WeatherUseCaseStatus.Idle,
            )
        )

    val weatherState: StateFlow<WeatherState<Weather?>> = _weatherState

    fun refreshCurrentWeather(location: GeoLocation) {
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
     * Fetches the current weather for the given location, no error handling, just result or null.
     */
    suspend fun getCurrentWeather(location: GeoLocation): Weather? = with(Dispatchers.IO) {
        val result = weatherRepository.getCurrentWeather(
            location.latitude,
            location.longitude,
            dataStorePreferences.useCelsiusUnits.first()
        )

        Log.d("CurrentWeatherUseCase", "Current weather: $result")
        return result.getOrNull()
    }
}
