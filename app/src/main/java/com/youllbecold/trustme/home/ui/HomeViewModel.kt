package com.youllbecold.trustme.home.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.data.location.GeoLocation
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.usecases.recommendation.RecommendationUseCase
import com.youllbecold.trustme.common.domain.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.common.domain.usecases.weather.HourlyWeatherUseCase
import com.youllbecold.trustme.common.domain.usecases.weather.model.WeatherUseCaseStatus
import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.common.ui.model.weatherwithrecommend.WeatherWithRecommendation
import com.youllbecold.trustme.home.ui.model.Forecast
import com.youllbecold.trustme.home.ui.model.HomeUiState
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime

/**
 * ViewModel for the home screen.
 */
@SuppressLint("MissingPermission") // Not missing, handled by the permission helper.
@KoinViewModel
class HomeViewModel(
    private val app: Application,
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val hourlyWeatherUseCase: HourlyWeatherUseCase,
    private val recommendUseCase: RecommendationUseCase,
    private val locationController: LocationController,
    permissionManager: LocationPermissionManager,
    networkStatusProvider: NetworkStatusProvider,
) : ViewModel() {

    /**
     * The UI state for the home screen.
     */
    val uiState: StateFlow<HomeUiState> = combine(
        permissionManager.hasLocationPermission,
        locationController.geoLocationState,
        networkStatusProvider.isConnected,
        currentWeatherUseCase.weatherState,
        hourlyWeatherUseCase.weatherState,
    ) { hasPermission, locationState, hasInternet, weatherState, hourlyWeatherState ->
        HomeUiState(
            hasPermission = hasPermission,
            status = when {
                !hasPermission -> LoadingStatus.Loading
                !hasInternet -> LoadingStatus.NoInternet
                !locationState.status.isIdle() -> locationState.status
                else -> weatherState.status.toWeatherStatus()
            },
            city = locationState.city,
            weather = obtainWeatherWithRecommendations(weatherState.weather, hourlyWeatherState.weather)
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, HomeUiState())

    init {
        locationController.refresh()

        // Wait for permission and internet connection to fetch the weather.
        combine(
            permissionManager.hasLocationPermission,
            networkStatusProvider.isConnected,
            locationController.geoLocationState,
        ) { hasPermission, hasInternet, geoLocation ->
            if (hasPermission && hasInternet) {
                geoLocation.location?.let { refreshWeather(it) }
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.RefreshWeather -> {
                viewModelScope.launch {
                    val location = locationController.geoLocationState.firstOrNull()?.location
                    location?.let { refreshWeather(it) }
                }
            }
        }
    }

    private fun refreshWeather(location: GeoLocation) {
        if (uiState.value.status == LoadingStatus.Loading) {
            return // Already in progress, do not call again.
        }

        if (PermissionChecker.hasLocationPermission(app)) {
            currentWeatherUseCase.refreshCurrentWeather(location)
            hourlyWeatherUseCase.refreshHourlyWeather(location, days = 2)
        }
    }

    private suspend fun obtainWeatherWithRecommendations(
        currentWeather: Weather?,
        hourlyWeather: List<Weather>,
    ): Forecast? {
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

    private fun WeatherUseCaseStatus.toWeatherStatus(): LoadingStatus = when (this) {
        WeatherUseCaseStatus.Idle,
        WeatherUseCaseStatus.Success -> LoadingStatus.Idle
        WeatherUseCaseStatus.Loading -> LoadingStatus.Loading
        is WeatherUseCaseStatus.Error -> LoadingStatus.GenericError
    }
}

/**
 * The actions that can be performed on the home screen.
 */
sealed class HomeAction {
    data object RefreshWeather : HomeAction()
}
