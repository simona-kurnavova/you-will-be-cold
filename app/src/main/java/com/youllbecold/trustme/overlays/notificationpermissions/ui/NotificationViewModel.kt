package com.youllbecold.trustme.overlays.notificationpermissions.ui

import androidx.lifecycle.ViewModel
import com.youllbecold.trustme.common.domain.notifications.DailyNotificationsManager
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for notification screen.
 */
@KoinViewModel
class NotificationViewModel(
    private val dailyNotificationsManager: DailyNotificationsManager
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
        dailyNotificationsManager.setDailyNotification(true)
    }

    /**
     * Set whether recommendation notification is allowed.
     */
    private fun setAllowRecommendNotification() {
        dailyNotificationsManager.setRecommendNotification(true)
    }
}

/**
 * Actions for notification screen.
 */
sealed class NotificationAction {
    data object SetAllowDailyNotification : NotificationAction()
    data object SetAllowRecommendNotification : NotificationAction()
}
