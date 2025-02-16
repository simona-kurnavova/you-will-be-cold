package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.preferences.DataStorePreferences
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for welcome screen.
 */
@KoinViewModel
class WelcomeViewModel(
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    /**
     * Handles [WelcomeAction].
     */
    fun onAction(action: WelcomeAction) {
        when (action) {
            is WelcomeAction.PassWelcomeScreen -> onWelcomeScreenPass()
        }
    }

    /**
     * Called when welcome screen is seen and passed by user.
     */
    private fun onWelcomeScreenPass() {
        viewModelScope.launch {
            dataStorePreferences.setWelcomeScreenShown(true)
        }
    }
}

/**
 * Actions for [WelcomeViewModel].
 */
sealed class WelcomeAction {
    /**
     * User passed the welcome screen.
     */
    data object PassWelcomeScreen : WelcomeAction()
}