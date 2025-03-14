package com.youllbecold.trustme.common.domain.notifications

import android.app.Application
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.data.preferences.DataStorePreferences
import com.youllbecold.trustme.common.domain.workers.DailyLogWorker
import com.youllbecold.trustme.common.domain.workers.DailyRecommendWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

/**
 * Manager for daily notifications.
 */
@Singleton
class DailyNotificationsManager(
    private val app: Application,
    private val dataStorePreferences: DataStorePreferences,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Whether daily notification is allowed.
     */
    val allowDailyNotification by dataStorePreferences::allowDailyNotification

    /**
     * Whether recommendation notification is allowed.
     */
    val allowRecommendNotification by dataStorePreferences::allowRecommendNotification

    init {
        coroutineScope.launch {
            // In case we lost permission, we should disable daily notifications
            if (!PermissionChecker.hasNotificationPermission(app)) {
                setDailyNotification(false)
                setRecommendNotification(false)
            }

            if (!PermissionChecker.hasBgLocationPermission(app)) {
                setRecommendNotification(false)
            }
        }
    }

    /**
     * Set whether daily notification is allowed.
     */
    fun setDailyNotification(enable: Boolean) {
        coroutineScope.launch {
            dataStorePreferences.setAllowDailyNotification(enable)

            if (enable) {
                DailyLogWorker.schedule(app)
            } else {
                DailyLogWorker.cancel(app)
            }
        }
    }

    /**
     * Set whether recommendation notification is allowed.
     */
    fun setRecommendNotification(enable: Boolean) {
        coroutineScope.launch {
            dataStorePreferences.setAllowRecommendNotification(enable)

            if (enable) {
                DailyRecommendWorker.schedule(app)
            } else {
                DailyRecommendWorker.cancel(app)
            }
        }
    }
}
