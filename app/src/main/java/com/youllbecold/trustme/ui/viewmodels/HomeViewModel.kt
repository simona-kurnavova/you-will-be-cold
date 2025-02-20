package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.usecases.weather.HourlyWeatherUseCase
import com.youllbecold.trustme.usecases.weather.state.WeatherUseCaseStatus
import com.youllbecold.trustme.utils.GeoLocation
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.NetworkHelper
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
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
@SuppressLint("MissingPermission") // Not missing, handled by the permission helper.
@KoinViewModel
class HomeViewModel(
    private val app: Application,
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val hourlyWeatherUseCase: HourlyWeatherUseCase,
    private val networkHelper: NetworkHelper,
    private val locationHelper: LocationHelper,
    permissionHelper: PermissionHelper,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            permissionHelper.hasLocationPermission,
            locationHelper.geoLocationState,
            networkHelper.isConnected,
            currentWeatherUseCase.weatherState,
            hourlyWeatherUseCase.weatherState,
        ) { hasPermission, locationState, hasInternet, weatherState, hourlyWeatherState ->
            HomeUiState(
                hasPermission = hasPermission,
                status = when {
                    !hasInternet -> LoadingStatus.NoInternet
                    !locationState.status.isIdle() -> locationState.status
                    else -> weatherState.status.toWeatherStatus()
                },
                currentWeather = weatherState.weather,
                hourlyTemperatures = hourlyWeatherState.weather.toHourlyTemperature(),
                city = locationState.city
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())

    init {
        refreshLocation()

        // Wait for permission and internet connection to fetch the weather.
        combine(
            permissionHelper.hasLocationPermission,
            networkHelper.isConnected,
            locationHelper.geoLocationState,
        ) { hasPermission, hasInternet, geoLocation ->
            if (hasPermission && hasInternet) {
                geoLocation.location?.let { refreshWeather(it) }
            }
        }.launchIn(viewModelScope)
    }

    private fun refreshLocation() {
        locationHelper.refresh()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.RefreshWeather -> {
                viewModelScope.launch {
                    val location = locationHelper.geoLocationState.firstOrNull()?.location
                    location?.let { refreshWeather(it) }
                }
            }
        }
    }

    private fun refreshWeather(location: GeoLocation) {
        if (uiState.value.status == LoadingStatus.Loading) {
            return // Already in progress, do not call again.
        }

        if (PermissionHelper.hasLocationPermission(app)) {
            currentWeatherUseCase.refreshCurrentWeather(location)
            hourlyWeatherUseCase.refreshHourlyWeather(location)
        }
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

    private fun WeatherUseCaseStatus.toWeatherStatus(): LoadingStatus = when (this) {
        WeatherUseCaseStatus.Idle,
        WeatherUseCaseStatus.Success -> LoadingStatus.Idle
        WeatherUseCaseStatus.Loading -> LoadingStatus.Loading
        is WeatherUseCaseStatus.Error -> LoadingStatus.GenericError
    }
}

/**
 * Limit the forecast to 24 hours only.
 */
private const val FORECAST_HOURS_LIMIT = 24

/**
 * UI state for the home screen.
 *
 * @param hasPermission Whether the app has location permission.
 * @param status The current status of the weather fetching, location refreshing, etc.
 * @param currentWeather The current weather.
 * @param hourlyTemperatures The hourly temperatures forecast.
 * @param city The city name.
 */
data class HomeUiState(
    val hasPermission: Boolean = false,
    val status: LoadingStatus = LoadingStatus.Idle,
    val currentWeather: Weather? = null,
    val hourlyTemperatures: List<HourlyTemperature> = emptyList(),
    val city: String? = null,
) {
    /**
     * Whether the screen is refreshing - does not account for the initial loading.
     */
    fun isRefreshing() = status == LoadingStatus.Loading && currentWeather != null

    /**
     * Whether the screen is loading for the first time.
     */
    fun isInitialLoading() = status == LoadingStatus.Loading && currentWeather == null
}

/**
 * The actions that can be performed on the home screen.
 */
sealed class HomeAction {
    data object RefreshWeather : HomeAction()
}

/**
 * The hourly temperature forecast for next few hours.
 */
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
