package com.youllbecold.trustme.recommend.home.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.recommend.home.ui.model.HomeUiState
import com.youllbecold.trustme.recommend.home.usecases.AllWeatherWithStatus
import com.youllbecold.trustme.recommend.home.usecases.FetchAllWeatherUseCase
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

/**
 * ViewModel for the home screen.
 */
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
        MutableStateFlow(AllWeatherWithStatus(status = LoadingStatus.Idle))

    private val loadingStatus: LoadingStatus
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
            when {
                !hasPermission -> allWeather.update { it.copy(LoadingStatus.MissingPermission) }
                !hasInternet -> allWeather.update {it.copy(LoadingStatus.NoInternet) }
                loadingStatus.isError()  // Recovery when we encountered an error
                        || loadingStatus.isIdle() // First time loading
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
        if (allWeather.value.status.isLoading()) {
            // Refresh already running, return
            return
        }

        allWeather.update { it.copy(status = LoadingStatus.Loading) }

        viewModelScope.launch {
            val useCelsius = unitsManager.fetchUnitsCelsius()

            allWeather.update {
                fetchAllWeatherUseCase.fetchWeather(useCelsius)
            }
        }
    }
}

/**
 * The actions that can be performed on the home screen.
 */
sealed class HomeAction {
    data object RefreshWeather : HomeAction()
}
