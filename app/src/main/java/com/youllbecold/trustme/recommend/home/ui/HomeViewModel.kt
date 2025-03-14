package com.youllbecold.trustme.recommend.home.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.ui.model.status.Error.MissingPermission
import com.youllbecold.trustme.common.ui.model.status.Error.NoInternet
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Loading
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.isError
import com.youllbecold.trustme.common.ui.model.status.isIdle
import com.youllbecold.trustme.common.ui.model.status.isLoading
import com.youllbecold.trustme.recommend.home.ui.model.HomeUiState
import com.youllbecold.trustme.recommend.home.usecases.AllWeatherWithStatus
import com.youllbecold.trustme.recommend.home.usecases.FetchAllWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * ViewModel for the home screen.
 */
@OptIn(ExperimentalAtomicApi::class)
@SuppressLint("MissingPermission") // Not missing, handled by the permission helper.
@KoinViewModel
class HomeViewModel(
    private val fetchAllWeatherUseCase: FetchAllWeatherUseCase,
    private val unitsManager: UnitsManager,
    locationController: LocationController,
    permissionManager: LocationPermissionManager,
    networkStatusProvider: NetworkStatusProvider,
) : ViewModel() {
    private val allWeather: MutableStateFlow<AllWeatherWithStatus> =
        MutableStateFlow(AllWeatherWithStatus(status = Idle))

    private val currentUseCelsius: StateFlow<Boolean?> by lazy {
        allWeather
            .map { it.useCelsiusUnits }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    private val status: Status
        get() = allWeather.value.status

    /**
     * The UI state for the home screen.
     */
    val uiState: StateFlow<HomeUiState> = allWeather.map { allWeather ->
        val city = allWeather.location?.let {
            locationController.quickGetCity(it.latitude, it.longitude)
        }

        HomeUiState(
            status = allWeather.status,
            city = city,
            forecast = allWeather.forecast,
            hourlyTemperature = allWeather.hourlyTemperatures,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, HomeUiState())

    init {
        locationController.refreshLocation()

        // Wait for permission and internet connection to fetch the weather.
        // Also serves as recovery mechanism after connection/permission was lost.
        combine(
            permissionManager.hasLocationPermission,
            networkStatusProvider.isConnected,
        ) { hasPermission, hasInternet ->
            Log.d(TAG, "Permission: $hasPermission, Internet: $hasInternet, Status: $status")
            when {
                !hasPermission -> allWeather.update { it.copy(MissingPermission) }
                !hasInternet -> allWeather.update {it.copy(NoInternet) }
                status.isError()  // Recovery when we encountered an error
                        || status.isIdle() // First time loading
                            -> updateWeatherAndRecommendations(unitsManager.fetchUnitsCelsius())
            }
        }.launchIn(viewModelScope)

        // Update the weather when the current and settings units don't match.
        combine(
            unitsManager.unitsCelsius, // Settings
            currentUseCelsius, // Actual
        ) { setUnitsCelsius, usingCelsius ->
            Log.d(TAG, "Settings: $setUnitsCelsius, Actual: $usingCelsius")

            if (usingCelsius != null && usingCelsius != setUnitsCelsius) {
                updateWeatherAndRecommendations(setUnitsCelsius)
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.RefreshWeather -> viewModelScope.launch {
                updateWeatherAndRecommendations(unitsManager.fetchUnitsCelsius())
            }
        }
    }

    private fun updateWeatherAndRecommendations(useCelsiusUnits: Boolean) {
        Log.d(TAG, "Updating weather with units: $useCelsiusUnits; Status: $status")

        if (status.isLoading()) {
            // Refresh already running, return
            return
        }

        allWeather.update { it.copy(status = Loading) }

        viewModelScope.launch {
            val weather = fetchAllWeatherUseCase.fetchWeather(useCelsiusUnits)
            Log.d(TAG, "Weather fetched: $weather")
            allWeather.update { weather }
        }
    }
}

/**
 * The actions that can be performed on the home screen.
 */
sealed class HomeAction {
    data object RefreshWeather : HomeAction()
}

private const val TAG = "HomeViewModel"
