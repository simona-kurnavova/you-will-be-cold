package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.trustme.weatherservice.WeatherProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the home screen.
 */
@KoinViewModel
class HomeViewModel(
    private val weatherProvider: WeatherProvider,
    permissionHelper: PermissionHelper
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
                if (hasPermission) {
                    weatherProvider.loadWeather()
                }
            }
        }
    }

    fun refreshWeather() {
        weatherProvider.loadWeather()
    }
}