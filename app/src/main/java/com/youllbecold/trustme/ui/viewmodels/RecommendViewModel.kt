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

@KoinViewModel
class RecommendViewModel(
    private val app: Application,
    private val weatherUseCase: RangedWeatherUseCase,
    private val dataStorePreferences: DataStorePreferences,
    private val recommendUseCase: RecommendationUseCase,
    private val locationHelper: LocationHelper
) : ViewModel() {
    private val _uiState: MutableStateFlow<RecommendUiState> = MutableStateFlow(RecommendUiState())

    /**
     * UI state for the recommendation screen.
     */
    val uiState: StateFlow<RecommendUiState> = _uiState

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

        viewModelScope.launch {
            if (!PermissionHelper.hasLocationPermission(app)) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            // TODO: refresh location when needed
            val location = locationHelper.geoLocationState.first()

            if (location.location == null) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            val weather = weatherUseCase.obtainRangedWeather(
                location.location,
                date,
                timeFrom,
                timeTo,
                dataStorePreferences.useCelsiusUnits.first()
            )

            val weatherRes = weather.getOrNull()

            if (weatherRes == null) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            val recommendation = recommendUseCase.recommend(weatherRes)

            if (recommendation == null) {
                _uiState.update { it.copy(status = LoadingStatus.GenericError) }
                return@launch
            }

            _uiState.update {
                it.copy(
                    weatherWithRecommendation = WeatherWithRecommendation(
                        weather = weatherRes.toPersistentList(),
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
