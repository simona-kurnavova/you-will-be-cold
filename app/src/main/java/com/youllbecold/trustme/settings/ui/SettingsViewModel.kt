package com.youllbecold.trustme.settings.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.notifications.DailyNotificationsManager
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.settings.ui.model.SettingsUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for settings screen.
 */
@KoinViewModel
class SettingsViewModel(
    private val app: Application,
    private val dailyNotificationsManager: DailyNotificationsManager,
    private val unitsManager: UnitsManager,
) : ViewModel() {

    /**
     * Flow of [SettingsUiState].
     */
    val uiState: StateFlow<SettingsUiState> = combine(
        dailyNotificationsManager.allowDailyNotification,
        dailyNotificationsManager.allowRecommendNotification,
        unitsManager.unitsCelsius
    ) { allowDailyNotification, allowRecommendNotification, useCelsiusUnits ->
        val notificationPermission = PermissionChecker.hasNotificationPermission(app)
        val allowDaily = allowDailyNotification && notificationPermission
        val allowRec = allowRecommendNotification && notificationPermission

        SettingsUiState(
            allowDailyNotification = allowDaily,
            allowRecommendNotification = allowRec,
            useCelsiusUnits = useCelsiusUnits
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    /**
     * Handles [SettingsAction]
     */
    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetAllowDailyNotification -> setAllowDailyNotification(action.allow)
            is SettingsAction.SetAllowRecommendNotification -> setAllowRecommendNotification(action.allow)
            is SettingsAction.SetUseCelsiusUnits -> setUseCelsiusUnits(action.useCelsius)
            else -> Unit // Handled in the UI
        }
    }

    /**
     * Set whether daily notification is allowed.
     *
     * @param allow Whether daily notification is allowed.
     */
    private fun setAllowDailyNotification(allow: Boolean) {
        dailyNotificationsManager.setDailyNotification(allow)
    }

    /**
     * Set whether recommendation notification is allowed.
     *
     * @param allow Whether recommendation notification is allowed.
     */
    private fun setAllowRecommendNotification(allow: Boolean) {
        dailyNotificationsManager.setRecommendNotification(allow)
    }

    /**
     * Set whether to use Celsius units.
     *
     * @param useCelsius Whether to use Celsius units.
     */
    private fun setUseCelsiusUnits(useCelsius: Boolean) {
        unitsManager.setUnitsCelsius(useCelsius)
    }
}

/**
 * Actions for settings screen.
 */
sealed class SettingsAction {
    data class SetAllowDailyNotification(val allow: Boolean) : SettingsAction()
    data class SetAllowRecommendNotification(val allow: Boolean) : SettingsAction()
    data class SetUseCelsiusUnits(val useCelsius: Boolean) : SettingsAction()
    data object SetupDailyNotification : SettingsAction()
    data object SetupRecommendNotification : SettingsAction()
}
