package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.usecases.CurrentWeatherStatus
import com.youllbecold.trustme.usecases.CurrentWeatherUseCase
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.weather.model.Weather
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the home screen.
 */
@SuppressLint("MissingPermission")
@KoinViewModel
class HomeViewModel(
    private val app: Application,
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    permissionHelper: PermissionHelper,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            permissionHelper.hasLocationPermission,
            currentWeatherUseCase.weatherState,
        ) { hasPermission, weatherState ->
            Log.d("HomeViewModel", "hasPermission=$hasPermission, weatherState=$weatherState")

            HomeUiState(
                hasPermission = hasPermission,
                status = when(weatherState.status) {
                    CurrentWeatherStatus.Idle,
                    CurrentWeatherStatus.Success -> WeatherStatus.Idle
                    CurrentWeatherStatus.Loading -> WeatherStatus.Loading
                    CurrentWeatherStatus.Error -> WeatherStatus.Error
                },
                currentWeather = weatherState.weather,
                city = weatherState.city
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())

    init {
        viewModelScope.launch {
            permissionHelper.hasLocationPermission.collectLatest { hasPermission ->
                Log.d("HomeViewModel", "Location permission state: $hasPermission")

                if (hasPermission) {
                    currentWeatherUseCase.refreshCurrentWeather()
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.RefreshCurrentWeather -> refreshCurrentWeather()
        }
    }

    private fun refreshCurrentWeather() {
        if (PermissionHelper.hasLocationPermission(app)) {
            currentWeatherUseCase.refreshCurrentWeather()
        }
    }
}

data class HomeUiState(
    val hasPermission: Boolean = false,
    val status: WeatherStatus = WeatherStatus.Idle,
    val currentWeather: Weather? = null,
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
    data object RefreshCurrentWeather : HomeAction()
}
