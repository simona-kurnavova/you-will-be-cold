package com.youllbecold.trustme.common.domain.usecases.weather

import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.domain.usecases.weather.model.ErrorType
import com.youllbecold.trustme.common.domain.usecases.weather.model.WeatherState
import com.youllbecold.trustme.common.domain.usecases.weather.model.WeatherUseCaseStatus
import com.youllbecold.trustme.common.domain.usecases.weather.model.copyWithError
import com.youllbecold.trustme.common.domain.usecases.weather.model.copyWithLoading
import com.youllbecold.trustme.common.domain.usecases.weather.model.copyWithNetworkResult
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
 * Use case for fetching and refreshing the hourly weather.
 */
@Singleton
class HourlyWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val unitsManager: UnitsManager,
    private val networkStatusProvider: NetworkStatusProvider
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var currentLocationAndDays: Pair<GeoLocation, Int>? = null

    private val _weatherState: MutableStateFlow<WeatherState<List<Weather>>> =
        MutableStateFlow(
            WeatherState(
                weather = emptyList(),
                status = WeatherUseCaseStatus.Idle
            )
        )

    /**
     * State flow for the hourly weather.
     */
    val weatherState: StateFlow<WeatherState<List<Weather>>> = _weatherState

    /**
     * Refreshes the hourly weather for the given location.
     */
    fun refreshHourlyWeather(location: GeoLocation, days: Int) {
        currentLocationAndDays = location to days
        _weatherState.update { it.copyWithLoading() }

        if (!networkStatusProvider.hasInternet()) {
            _weatherState.update { it.copyWithError(ErrorType.OFFLINE)  }
            return
        }

        coroutineScope.launch {
            val result = weatherRepository.getHourlyWeather(
                location.latitude,
                location.longitude,
                unitsManager.unitsCelsius.first(),
                forecastDays = days
            )

            _weatherState.update { it.copyWithNetworkResult(result) }
        }
    }

    /**
     * Called when the units are changed, to quick refresh the hourly weather.
     */
    fun onUnitsChanged() {
        currentLocationAndDays?.let { refreshHourlyWeather(it.first, it.second) }
    }
}