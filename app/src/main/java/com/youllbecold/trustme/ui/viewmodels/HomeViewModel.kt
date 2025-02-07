package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.usecases.weather.HourlyWeatherUseCase
import com.youllbecold.trustme.usecases.weather.state.WeatherUseCaseStatus
import com.youllbecold.trustme.utils.Location
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

/**
 * ViewModel for the home screen.
 */
@SuppressLint("MissingPermission")
@KoinViewModel
class HomeViewModel(
    private val app: Application,
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val hourlyWeatherUseCase: HourlyWeatherUseCase,
    permissionHelper: PermissionHelper,
) : ViewModel() {

    private val locationError: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> =
        combine(
            permissionHelper.hasLocationPermission,
            locationError,
            currentWeatherUseCase.weatherState,
            hourlyWeatherUseCase.weatherState
        ) { hasPermission, locationError, weatherState, hourlyWeatherState ->
            Log.d("HomeViewModel", "hasPermission=$hasPermission, weatherState=$weatherState")

            HomeUiState(
                hasPermission = hasPermission,
                status = if (locationError) WeatherStatus.Error else weatherState.status.toWeatherStatus(),
                currentWeather = weatherState.weather,
                hourlyTemperatures = hourlyWeatherState.weather.toHourlyTemperature(),
                city = weatherState.city
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())

    init {
        viewModelScope.launch {
            permissionHelper.hasLocationPermission.collectLatest { hasPermission ->
                Log.d("HomeViewModel", "Location permission state: $hasPermission")

                if (hasPermission) {
                    refreshWeather()
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.RefreshWeather -> refreshWeather()
        }
    }

    private fun refreshWeather() {
        if (PermissionHelper.hasLocationPermission(app)) {
            LocationHelper.refreshLocation(
                app,
                onSuccess = ::onLocationObtained,
                onError = { locationError.value = true }
            )
        }
    }

    private fun onLocationObtained(location: Location) {
        locationError.value = false
        currentWeatherUseCase.refreshCurrentWeather(location)
        hourlyWeatherUseCase.refreshHourlyWeather(location)
    }

    private fun List<Weather>.toHourlyTemperature(): List<HourlyTemperature> =
        filter { it.time > LocalDateTime.now() }
            .take(FORECAST_HOURS_LIMIT)
            .map { weather ->
                HourlyTemperature(
                    time = weather.time,
                    temperature = weather.temperature,
                    weatherEvaluation = weather.weatherEvaluation
                )
            }

    private fun WeatherUseCaseStatus.toWeatherStatus(): WeatherStatus = when (this) {
        WeatherUseCaseStatus.Idle,
        WeatherUseCaseStatus.Success -> WeatherStatus.Idle
        WeatherUseCaseStatus.Loading -> WeatherStatus.Loading
        is WeatherUseCaseStatus.Error -> WeatherStatus.Error
    }
}

/**
 * Limit the forecast to 24 hours only.
 */
private const val FORECAST_HOURS_LIMIT = 24

data class HomeUiState(
    val hasPermission: Boolean = false,
    val status: WeatherStatus = WeatherStatus.Idle,
    val currentWeather: Weather? = null,
    val hourlyTemperatures: List<HourlyTemperature> = emptyList(),
    val city: String? = null,
) {
    fun isRefreshing() = status == WeatherStatus.Loading && currentWeather != null
}

enum class WeatherStatus {
    Idle,
    Loading,
    Error
}

sealed class HomeAction {
    data object RefreshWeather : HomeAction()
}

data class HourlyTemperature(
    val time: LocalDateTime,
    val temperature: Double,
    val weatherEvaluation: WeatherEvaluation
) {
    val formattedTime: String
        get() {
            val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            return time.format(formatter)
        }

    val roundedTemperature: Int
        get() = temperature.roundToInt()
}
