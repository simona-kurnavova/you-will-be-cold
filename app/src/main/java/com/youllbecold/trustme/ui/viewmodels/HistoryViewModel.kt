package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.ui.viewmodels.state.LogState
import com.youllbecold.trustme.ui.viewmodels.state.toLogData
import com.youllbecold.trustme.ui.viewmodels.state.toLogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the history screen.
 */
@KoinViewModel
class HistoryViewModel(
    private val logRepository: LogRepository,
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    /**
     * UI state for the history screen.
     */
    val uiState: Flow<PagingData<LogState>> = logRepository
        .getAllWithPaging()
        .map { pagingData ->
            pagingData.map { it.toLogState(dataStorePreferences.useCelsiusUnits.first()) }
        }
        .cachedIn(viewModelScope)

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.Delete -> {
                viewModelScope.launch {
                    logRepository.deleteLog(action.state.toLogData())
                }
            }
            is HistoryAction.Edit -> Unit // Do nothing, will be handled by the navigation
        }
    }
}

/**
 * Actions for the history screen.
 */
sealed class HistoryAction {
    data class Edit(val state: LogState) : HistoryAction()
    data class Delete(val state: LogState) : HistoryAction()
}
