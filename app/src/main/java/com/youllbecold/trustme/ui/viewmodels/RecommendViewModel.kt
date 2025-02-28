package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.ui.viewmodels.state.LoadingStatus
import com.youllbecold.trustme.ui.viewmodels.state.WeatherWithRecommendation
import com.youllbecold.trustme.usecases.recommendation.RecommendationUseCase
import com.youllbecold.trustme.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.NetworkHelper
import com.youllbecold.trustme.utils.PermissionHelper
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDate
import java.time.LocalTime

/**
 * ViewModel for the recommendation screen.
 */
@KoinViewModel
class RecommendViewModel(
    private val app: Application,
    private val weatherUseCase: RangedWeatherUseCase,
    private val dataStorePreferences: DataStorePreferences,
    private val recommendUseCase: RecommendationUseCase,
    private val locationHelper: LocationHelper,
    private val networkHelper: NetworkHelper,
) : ViewModel() {
    private val _uiState: MutableStateFlow<RecommendUiState> = MutableStateFlow(RecommendUiState())

    /**
     * UI state for the recommendation screen.
     */
    val uiState: StateFlow<RecommendUiState> = _uiState

    init {
        // We probably already have location, but just in case, refresh it.
        locationHelper.refresh()
    }

    /**
     * Handles [RecommendAction].
     */
    fun onAction(action: RecommendAction) {
        when (action) {
            is RecommendAction.UpdateRecommendation -> updateWeatherAndRecomm(
                action.date,
                action.timeFrom,
                action.timeTo,
            )
        }
    }

    @SuppressLint("MissingPermission") // It is checked
    private fun updateWeatherAndRecomm(
        date: LocalDate,
        timeFrom: LocalTime,
        timeTo: LocalTime,
    ) {
        _uiState.update { RecommendUiState(status = LoadingStatus.Loading) }

        when {
            !PermissionHelper.hasLocationPermission(app) -> {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return
            }

            !networkHelper.hasInternet() -> {
                _uiState.update { it.copy(status = LoadingStatus.NoInternet) }
                return
            }
        }

        viewModelScope.launch {
            val location = locationHelper.simpleLocation // Readily available
                ?: LocationHelper.getLastLocation(app) // Ask for it otherwise

            if (location == null) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            val weather = weatherUseCase.obtainRangedWeather(
                location = location,
                date = date,
                timeFrom = timeFrom,
                timeTo = timeTo,
                useCelsiusUnits = dataStorePreferences.useCelsiusUnits.first()
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
        val date: LocalDate,
        val timeFrom: LocalTime,
        val timeTo: LocalTime,
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
