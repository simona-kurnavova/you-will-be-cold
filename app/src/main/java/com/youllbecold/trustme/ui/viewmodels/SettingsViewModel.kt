package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.preferences.DataStorePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for settings screen.
 */
@KoinViewModel
class SettingsViewModel(private val dataStore: DataStorePreferences) : ViewModel() {

    /**
     * Flow of whether daily notification is allowed.
     */
    val allowDailyNotification: Flow<Boolean> = dataStore.allowDailyNotification

    /**
     * Set whether daily notification is allowed.
     *
     * @param allow Whether daily notification is allowed.
     */
    fun setAllowDailyNotification(allow: Boolean) {
        viewModelScope.launch {
            dataStore.setAllowDailyNotification(allow)
        }
    }
}