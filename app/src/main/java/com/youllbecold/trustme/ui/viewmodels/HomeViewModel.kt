package com.youllbecold.trustme.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.trustme.weatherservice.WeatherProvider
import com.youllbecold.trustme.weatherservice.model.WeatherNow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the home screen.
 */
@KoinViewModel
class HomeViewModel(
    private val weatherProvider: WeatherProvider,
    permissionHelper: PermissionHelper,
) : ViewModel() {

    /**
     * Current weather.
     */
    val currentWeather by weatherProvider::currentWeather

    /**
     * Location permission state.
     */
    val locationGranted by permissionHelper::locationState

    init {
        viewModelScope.launch {
            permissionHelper.locationState.collectLatest { hasPermission ->
                Log.d("HomeViewModel", "Location permission state: $hasPermission")
                if (hasPermission) {
                   refreshWeather()
                }
            }
        }
    }

    fun refreshWeather() {
        weatherProvider.loadWeather()
    }
}

sealed class WeatherState {
    data object Idle : WeatherState()
    data object Loading : WeatherState()
    data class Success(val weather: WeatherNow) : WeatherState()
    data object Error : WeatherState()
}
