package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.viewmodels.state.LoadingStatus
import com.youllbecold.trustme.ui.viewmodels.state.WeatherWithRecommendation
import com.youllbecold.trustme.ui.viewmodels.state.isIdle
import com.youllbecold.trustme.usecases.recommendation.RecommendationUseCase
import com.youllbecold.trustme.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.usecases.weather.HourlyWeatherUseCase
import com.youllbecold.trustme.usecases.weather.state.WeatherUseCaseStatus
import com.youllbecold.trustme.utils.GeoLocation
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.NetworkHelper
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList
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
    private val recommendUseCase: RecommendationUseCase,
    private val locationHelper: LocationHelper,
    permissionHelper: PermissionHelper,
    networkHelper: NetworkHelper,
) : ViewModel() {

    /**
     * The UI state for the home screen.
     */
    val uiState: StateFlow<HomeUiState> = combine(
        permissionHelper.hasLocationPermission,
        locationHelper.geoLocationState,
        networkHelper.isConnected,
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
        locationHelper.refresh()

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

/**
 * The hourly temperature forecast for next few hours.
 */
@Immutable
data class HourlyTemperature(
    private val timeMillis: Long,
    val temperature: Int,
    val weatherIcon: IconType
) {
    val formattedTime: String
        get() {
            val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            return timeMillis.millisToDateTime.format(formatter)
        }
}

/**
 * UI state for the home screen.
 *
 * @param hasPermission Whether the app has location permission.
 * @param status The current status of the weather fetching, location refreshing, etc.
 * @param city The city name.
 * @param weather Forecast for the current, today, and tomorrow weather, with recommendations.
 */
@Stable
data class HomeUiState(
    val hasPermission: Boolean = false,
    val status: LoadingStatus = LoadingStatus.Idle,
    val city: String? = null,
    val weather: Forecast? = null
) {
    /**
     * Whether the screen is refreshing - does not account for the initial loading.
     */
    fun isRefreshing() = status == LoadingStatus.Loading && weather != null

    /**
     * Whether the screen is loading for the first time.
     */
    fun isInitialLoading() = status == LoadingStatus.Loading && weather == null

    /**
     * Whether the screen is in an error state.
     */
    fun isError() = status == LoadingStatus.GenericError || status == LoadingStatus.NoInternet
}

@Stable
data class Forecast(
    val current: WeatherWithRecommendation,
    val today: WeatherWithRecommendation,
    val tomorrow: WeatherWithRecommendation
) {
    /**
     * The next 24 hours of weather forecast.
     */
    fun next24Hours(): PersistentList<HourlyTemperature> =
        (today.weather + tomorrow.weather)
            .take(HOURS_IN_HOURLY_WEATHER)
            .toHourlyTemperature()
            .toPersistentList()

    private fun List<Weather>?.toHourlyTemperature(): PersistentList<HourlyTemperature> =
        this?.map { weather ->
            HourlyTemperature(
                timeMillis = weather.time,
                temperature = weather.temperature.roundToInt(),
                weatherIcon = weather.weatherEvaluation.icon
            )
        }?.toPersistentList() ?: persistentListOf()
}

private const val HOURS_IN_HOURLY_WEATHER = 24
