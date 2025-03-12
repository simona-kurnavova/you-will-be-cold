package com.youllbecold.trustme.recommend.home.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.domain.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.recommend.home.usecases.HourlyWeatherUseCase
import com.youllbecold.trustme.common.ui.components.utils.formatTime
import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.recommend.home.ui.model.Forecast
import com.youllbecold.trustme.recommend.home.ui.model.HomeUiState
import com.youllbecold.trustme.recommend.home.ui.model.HourlyTemperature
import com.youllbecold.trustme.recommend.usecases.RecommendationUseCase
import com.youllbecold.trustme.recommend.usecases.model.WeatherWithRecommendation
import com.youllbecold.trustme.recommend.usecases.model.mappers.icon
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime
import kotlin.math.roundToInt

/**
 * ViewModel for the home screen.
 */
@SuppressLint("MissingPermission") // Not missing, handled by the permission helper.
@KoinViewModel
class HomeViewModel(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val hourlyWeatherUseCase: HourlyWeatherUseCase,
    private val recommendUseCase: RecommendationUseCase,
    private val unitsManager: UnitsManager,
    locationController: LocationController,
    permissionManager: LocationPermissionManager,
    networkStatusProvider: NetworkStatusProvider,
) : ViewModel() {
    private val loadingStatus: MutableStateFlow<LoadingStatus> = MutableStateFlow(LoadingStatus.Idle)
    private val forecastState: MutableStateFlow<Forecast?> = MutableStateFlow(null)

    private val hourlyTemperature: StateFlow<PersistentList<HourlyTemperature>> = forecastState.map { forecast ->
        forecast
            ?.let { createHourlyTemperatures(it) }
            ?: persistentListOf()
    }.stateIn(viewModelScope, SharingStarted.Lazily, persistentListOf())

    /**
     * The UI state for the home screen.
     */
    // TODO figure out the location
    val uiState: StateFlow<HomeUiState> = combine(
        locationController.geoLocationState,
        loadingStatus,
        forecastState,
        hourlyTemperature
    ) { geoLocation, loadingStatus, forecast, hourlyTemperature ->
        HomeUiState(
            status = loadingStatus,
            city = geoLocation.city,
            forecast = forecast,
            hourlyTemperature = hourlyTemperature,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, HomeUiState())

    init {
        locationController.refresh()

        // Wait for permission and internet connection to fetch the weather.
        // Also serves as recovery mechanism after connection/permission was lost.
        combine(
            permissionManager.hasLocationPermission,
            networkStatusProvider.isConnected,
        ) { hasPermission, hasInternet ->
            when {
                !hasPermission -> loadingStatus.update { LoadingStatus.MissingPermission }
                !hasInternet -> loadingStatus.update { LoadingStatus.NoInternet }
                forecastState.value == null // First time load
                        || loadingStatus.value.isError() // Recovery when we encountered an error
                        || loadingStatus.value.isIdle() // Sanity check for default state (nothing happened yet)
                            -> updateWeatherAndRecommendations()
            }
        }.launchIn(viewModelScope)

        // Update the weather when the units change.
        unitsManager.unitsCelsius.onEach {
            updateWeatherAndRecommendations()
        }.launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.RefreshWeather -> updateWeatherAndRecommendations()
        }
    }

    private fun updateWeatherAndRecommendations() {
        if (loadingStatus.value.isLoading()) {
            // Refresh already running, return
            return
        }

        loadingStatus.update { LoadingStatus.Loading }

        viewModelScope.launch {
            val useCelsius = unitsManager.fetchUnitsCelsius()
            val current = currentWeatherUseCase.fetchCurrentWeather(useCelsius)
            val hourly = hourlyWeatherUseCase.fetchHourlyWeather(useCelsius, 2)

            val forecast = obtainWeatherWithRecommendations(current.weather, hourly.weather)

            forecastState.update { forecast }
            loadingStatus.update { LoadingStatus.Success }
        }
    }

    private suspend fun obtainWeatherWithRecommendations(
        currentWeather: Weather?,
        hourlyWeather: List<Weather>,
    ): Forecast? {
        // TODO: Extract to usecase
        if (currentWeather == null || hourlyWeather.isEmpty()) {
            return null
        }

        val now = LocalDateTime.now()

        val (allDayTodayWeather, tomorrowWeather) = hourlyWeather
            .partition { it.time.millisToDateTime.toLocalDate() == now.toLocalDate() }

        val todayWeather = allDayTodayWeather
            .filter { it.time.millisToDateTime.toLocalTime() >= now.toLocalTime() }

        return Forecast(
            current = WeatherWithRecommendation(
                persistentListOf(currentWeather),
                recommendUseCase.recommend(listOf(currentWeather))
            ),
            today = WeatherWithRecommendation(
                todayWeather.toPersistentList(),
                recommendUseCase.recommend(todayWeather)
            ),
            tomorrow = WeatherWithRecommendation(
                tomorrowWeather.toPersistentList(),
                recommendUseCase.recommend(tomorrowWeather)
            )
        )
    }

    /**
     * The next 24 hours of weather temperature forecast.
     */
    private fun createHourlyTemperatures(forecast: Forecast): PersistentList<HourlyTemperature> =
        (forecast.today.weather + forecast.tomorrow.weather)
            .take(HOURS_IN_HOURLY_WEATHER)
            .toHourlyTemperature()
            .toPersistentList()

    private fun List<Weather>?.toHourlyTemperature(): PersistentList<HourlyTemperature> =
        this?.map { weather ->
            HourlyTemperature(
                formattedTime = weather.time.formatTime(),
                temperature = weather.temperature.roundToInt(),
                weatherIcon = weather.weatherEvaluation.icon
            )
        }?.toPersistentList() ?: persistentListOf()

    private fun Long.formatTime(): String =
        this.millisToDateTime
            .toLocalTime()
            .formatTime()
}

/**
 * The actions that can be performed on the home screen.
 */
sealed class HomeAction {
    data object RefreshWeather : HomeAction()
}

private const val HOURS_IN_HOURLY_WEATHER = 24
