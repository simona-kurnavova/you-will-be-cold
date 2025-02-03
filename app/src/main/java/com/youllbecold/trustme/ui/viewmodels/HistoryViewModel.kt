package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.database.LogDao
import com.youllbecold.trustme.database.entity.LogEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the history screen.
 */
@KoinViewModel
class HistoryViewModel(
    private val logDao: LogDao
) : ViewModel() {

    val logs: StateFlow<List<LogEntity>>
        get() = logDao.getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
