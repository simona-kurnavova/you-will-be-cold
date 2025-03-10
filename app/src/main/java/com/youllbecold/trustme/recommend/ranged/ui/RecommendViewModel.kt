package com.youllbecold.trustme.recommend.ranged.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.recommend.ranged.ui.model.RecommendUiState
import com.youllbecold.trustme.recommend.usecases.RecommendForDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the recommendation screen.
 */
@KoinViewModel
class RecommendViewModel(
    private val unitsManager: UnitsManager,
    private val recommendForDateUseCase: RecommendForDateUseCase,
) : ViewModel() {
    private var currentDateTimeRange: DateTimeState? = null

    private val _uiState: MutableStateFlow<RecommendUiState> = MutableStateFlow(RecommendUiState())

    /**
     * UI state for the recommendation screen.
     */
    val uiState: StateFlow<RecommendUiState> = _uiState

    init {
        // Update recommendation reactively on units change.
        unitsManager.unitsCelsius.onEach {
            currentDateTimeRange?.let { updateWeatherAndRecommendation(it) }
        }.launchIn(viewModelScope)
    }

    /**
     * Handles [RecommendAction].
     */
    fun onAction(action: RecommendAction) {
        when (action) {
            is RecommendAction.UpdateRecommendation -> updateWeatherAndRecommendation(action.datetimeRange)
        }
    }

    private fun updateWeatherAndRecommendation(datetimeRange: DateTimeState) {
        _uiState.update { RecommendUiState(status = LoadingStatus.Loading) }

        viewModelScope.launch {
            val recommendState = recommendForDateUseCase.recommendForDate(
                datetimeRange = datetimeRange,
                useCelsius = unitsManager.unitsCelsius.first()
            )

            _uiState.update {
                it.copy(
                    status = recommendState.status,
                    weatherWithRecommendation = recommendState.weatherWithRecommendation
                )
            }

            currentDateTimeRange = recommendState.dateTimeState
        }
    }
}

/**
 * Actions for the recommendation screen.
 */
sealed class RecommendAction {
    data class UpdateRecommendation(val datetimeRange: DateTimeState) : RecommendAction()
}
