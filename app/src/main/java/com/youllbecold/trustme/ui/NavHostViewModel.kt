package com.youllbecold.trustme.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.domain.welcome.WelcomeOverlayManager
import com.youllbecold.trustme.ui.model.OverlayState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the main composable.
 */
@KoinViewModel
class NavHostViewModel(
    permissionManager: LocationPermissionManager,
    welcomeOverlayManager: WelcomeOverlayManager
) : ViewModel() {

    /**
     * State flow for overlay state - what kind of overlay to show or none.
     */
    val overlayState: StateFlow<OverlayState> = combine(
        permissionManager.hasLocationPermission,
        welcomeOverlayManager.welcomeScreenShown
    ) { locationState, welcomeScreenShown ->
        Log.d("NavHostViewModel", "Location state: $locationState, welcome screen shown: $welcomeScreenShown")

        when {
            !welcomeScreenShown -> OverlayState.NEW_USER
            !locationState -> OverlayState.LOCATION_PERM_MISSING
            else -> OverlayState.NONE
        }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, OverlayState.UNDEFINED)

    init {
        permissionManager.refreshLocationPermissionState()
    }
}
