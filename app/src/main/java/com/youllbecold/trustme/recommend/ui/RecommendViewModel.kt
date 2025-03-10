package com.youllbecold.trustme.recommend.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.domain.usecases.recommendation.RecommendationUseCase
import com.youllbecold.trustme.common.domain.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.common.ui.model.weatherwithrecommend.WeatherWithRecommendation
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the recommendation screen.
 */
@KoinViewModel
class RecommendViewModel(
    private val app: Application,
    private val weatherUseCase: RangedWeatherUseCase,
    private val unitsManager: UnitsManager,
    private val recommendUseCase: RecommendationUseCase,
    private val locationController: LocationController,
    private val networkStatusProvider: NetworkStatusProvider,
) : ViewModel() {
    private val _uiState: MutableStateFlow<RecommendUiState> = MutableStateFlow(RecommendUiState())

    /**
     * UI state for the recommendation screen.
     */
    val uiState: StateFlow<RecommendUiState> = _uiState

    init {
        // We probably already have location, but just in case, refresh it.
        locationController.refresh()
    }

    /**
     * Handles [RecommendAction].
     */
    fun onAction(action: RecommendAction) {
        when (action) {
            is RecommendAction.UpdateRecommendation -> updateWeatherAndRecomm(action.datetimeRange)
        }
    }

    @SuppressLint("MissingPermission") // It is checked
    private fun updateWeatherAndRecomm(
        datetimeRange: DateTimeState
    ) {
        _uiState.update { RecommendUiState(status = LoadingStatus.Loading) }

        when {
            !PermissionChecker.hasLocationPermission(app) -> {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return
            }

            !networkStatusProvider.hasInternet() -> {
                _uiState.update { it.copy(status = LoadingStatus.NoInternet) }
                return
            }
        }

        viewModelScope.launch {
            val location = locationController.simpleLocation // Readily available
                ?: LocationController.getLastLocation(app) // Ask for it otherwise

            if (location == null) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            val weather = weatherUseCase.obtainRangedWeather(
                location = location,
                date = datetimeRange.date.localDate,
                timeFrom = datetimeRange.timeFrom.localTime,
                timeTo = datetimeRange.timeTo.localTime,
                useCelsiusUnits = unitsManager.unitsCelsius.first()
            ).getOrNull()

            if (weather == null) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            val recommendation = recommendUseCase.recommend(weather)

            if (recommendation == null) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            _uiState.update {
                it.copy(
                    weatherWithRecommendation = WeatherWithRecommendation(
                        weather = weather.toPersistentList(),
                        recommendation = recommendation
                    ),
                    status = LoadingStatus.Idle
                )
            }
        }
    }
}

/**
 * Actions for the recommendation screen.
 */
sealed class RecommendAction {
    data class UpdateRecommendation(
        val datetimeRange: DateTimeState
    ) : RecommendAction()
}

/**
 * UI state for the recommendation screen.
 */
@Stable
data class RecommendUiState(
    val weatherWithRecommendation: WeatherWithRecommendation? = null,
    val status: LoadingStatus = LoadingStatus.Idle,
)
