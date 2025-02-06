package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.utils.Location
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.model.WeatherNow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the home screen.
 */
@SuppressLint("MissingPermission")
@KoinViewModel
class HomeViewModel(
    private val app: Application,
    private val weatherRepository: WeatherRepository,
    private val locationHelper: LocationHelper,
    private val dataStorePreferences: DataStorePreferences,
    permissionHelper: PermissionHelper,
) : ViewModel() {

    private var currentCity: String? = null

    private val loadingState: MutableStateFlow<WeatherStatus> = MutableStateFlow(WeatherStatus.Idle)

    val uiState: StateFlow<HomeUiState> =
        combine(
            permissionHelper.locationState,
            weatherRepository.currentWeather,
            loadingState
        ) { hasPermission, currentWeather, loadingState ->
            HomeUiState(
                hasPermission = hasPermission,
                status = loadingState,
                currentWeather = currentWeather,
                city = currentCity
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())

    init {
        viewModelScope.launch {
            permissionHelper.locationState.collectLatest { hasPermission ->
                Log.d("HomeViewModel", "Location permission state: $hasPermission")

                if (hasPermission) {
                    refreshLocationAndWeather()
                }
            }
        }
    }

    fun refreshLocationAndWeather() {
        loadingState.update { WeatherStatus.Loading }

        viewModelScope.launch {
            locationHelper.refreshLocation(
                app,
                onSuccess = ::onLocationObtained,
                onError = { loadingState.update { WeatherStatus.Error } }
            )
        }
    }

    private fun onLocationObtained(location: Location) {
        currentCity = location.city

        viewModelScope.launch {
            val useCelsius = dataStorePreferences.useCelsiusUnits.first()
            val result = weatherRepository.getCurrentWeather(location.latitude, location.longitude, useCelsius)

            loadingState.update {
                if (result.isSuccess) WeatherStatus.Idle else WeatherStatus.Error
            }
        }
    }
}

data class HomeUiState(
    val hasPermission: Boolean = false,
    val status: WeatherStatus = WeatherStatus.Idle,
    val currentWeather: WeatherNow? = null,
    val city: String? = null,
) {
    fun isRefreshing() = status == WeatherStatus.Loading && currentWeather != null
}

enum class WeatherStatus {
    Idle,
    Loading,
    Error
}
