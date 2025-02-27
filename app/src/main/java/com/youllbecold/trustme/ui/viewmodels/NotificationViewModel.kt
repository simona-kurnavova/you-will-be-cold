package com.youllbecold.trustme.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.trustme.worker.DailyLogWorker
import com.youllbecold.trustme.worker.DailyRecommendWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for notification screen.
 */
@KoinViewModel
class NotificationViewModel(
    private val app: Application,
    private val dataStore: DataStorePreferences
) : ViewModel() {

    /**
     * Handles [NotificationAction]
     */
    fun onAction(action: NotificationAction) {
        when (action) {
            is NotificationAction.SetAllowDailyNotification -> setAllowDailyNotification()
            is NotificationAction.SetAllowRecommendNotification -> setAllowRecommendNotification()
        }
    }

    /**
     * Set whether daily notification is allowed.
     */
    private fun setAllowDailyNotification() {
        viewModelScope.launch {
            dataStore.setAllowDailyNotification(true)
            DailyLogWorker.schedule(app)
        }
    }

    /**
     * Set whether recommendation notification is allowed.
     */
    private fun setAllowRecommendNotification() {
        viewModelScope.launch {
            dataStore.setAllowRecommendNotification(true)
            DailyRecommendWorker.schedule(app)
        }
    }
}

/**
 * Actions for notification screen.
 */
sealed class NotificationAction {
    data object SetAllowDailyNotification : NotificationAction()
    data object SetAllowRecommendNotification : NotificationAction()
}
