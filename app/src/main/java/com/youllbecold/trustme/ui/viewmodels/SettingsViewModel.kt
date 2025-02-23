package com.youllbecold.trustme.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.trustme.worker.DailyLogWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for settings screen.
 */
@KoinViewModel
class SettingsViewModel(
    private val app: Application,
    private val dataStore: DataStorePreferences
) : ViewModel() {

    /**
     * Flow of [SettingsUiState].
     */
    val uiState: StateFlow<SettingsUiState> = combine(
        dataStore.allowDailyNotification,
        dataStore.useCelsiusUnits
    ) { allowDailyNotification, useCelsiusUnits ->
        val allowDaily = allowDailyNotification && PermissionHelper.hasNotificationPermission(app)

        SettingsUiState(
            allowDailyNotification = allowDaily,
            useCelsiusUnits = useCelsiusUnits
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    init {
        // In case we lost permission, we should disable daily notification toggle
        if (!PermissionHelper.hasNotificationPermission(app)) {
            setAllowDailyNotification(false)
        }
    }

    /**
     * Handles [SettingsAction]
     */
    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetAllowDailyNotification -> setAllowDailyNotification(action.allow)
            is SettingsAction.SetUseCelsiusUnits -> setUseCelsiusUnits(action.useCelsius)
        }
    }

    /**
     * Set whether daily notification is allowed.
     *
     * @param allow Whether daily notification is allowed.
     */
    private fun setAllowDailyNotification(allow: Boolean) {
        viewModelScope.launch {
            dataStore.setAllowDailyNotification(allow)

            if (allow) {
                DailyLogWorker.schedule(app)
            } else {
                DailyLogWorker.cancel(app)
            }
        }
    }

    /**
     * Set whether to use Celsius units.
     *
     * @param useCelsius Whether to use Celsius units.
     */
    private fun setUseCelsiusUnits(useCelsius: Boolean) {
        viewModelScope.launch {
            dataStore.setUseCelsiusUnits(useCelsius)
        }
    }
}

/**
 * UI state for settings screen.
 */
data class SettingsUiState(
    val allowDailyNotification: Boolean = false,
    val useCelsiusUnits: Boolean = false
)

/**
 * Actions for settings screen.
 */
sealed class SettingsAction {
    data class SetAllowDailyNotification(val allow: Boolean) : SettingsAction()
    data class SetUseCelsiusUnits(val useCelsius: Boolean) : SettingsAction()
}
