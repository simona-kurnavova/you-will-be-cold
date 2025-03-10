package com.youllbecold.trustme.overlays.welcome.ui

import androidx.lifecycle.ViewModel
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.domain.welcome.WelcomeOverlayManager
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for welcome screen.
 */
@KoinViewModel
class WelcomeViewModel(
    private val locationPermissionManager: LocationPermissionManager,
    private val welcomeOverlayManager: WelcomeOverlayManager,
) : ViewModel() {

    val hasLocationPermission: Boolean
        get() = locationPermissionManager.hasLocationPermission.value

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
        welcomeOverlayManager.onWelcomeScreenPass()
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
