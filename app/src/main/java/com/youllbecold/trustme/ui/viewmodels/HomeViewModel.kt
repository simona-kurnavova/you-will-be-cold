package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.weatherservice.WeatherProvider
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the home screen.
 */
@KoinViewModel
class HomeViewModel(
    weatherProvider: WeatherProvider
) : ViewModel() {

    /**
     * Current weather.
     */
    val currentWeather by weatherProvider::currentWeather

    init {
        viewModelScope.launch { weatherProvider.loadWeather() }
    }
}