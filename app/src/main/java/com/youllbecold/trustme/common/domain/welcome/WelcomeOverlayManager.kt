package com.youllbecold.trustme.common.domain.welcome

import com.youllbecold.trustme.common.data.preferences.DataStorePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

/**
 * Manager for the welcome screen.
 */
@Singleton
class WelcomeOverlayManager(
    private val dataStorePreferences: DataStorePreferences,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Flow of whether the welcome screen has been shown.
     */
    val welcomeScreenShown: Flow<Boolean> by dataStorePreferences::welcomeScreenShown

    /**
     * Should be called on welcome screen first pass.
     */
    fun onWelcomeScreenPass() {
        coroutineScope.launch {
            dataStorePreferences.setWelcomeScreenShown(true)
        }
    }
}
