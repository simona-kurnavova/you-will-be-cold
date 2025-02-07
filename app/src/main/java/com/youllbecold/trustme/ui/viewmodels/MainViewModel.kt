package com.youllbecold.trustme.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.utils.PermissionHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val permissionHelper: PermissionHelper,
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    /**
     * State flow for overlay state - what kind of overlay to show or none.
     */
    val overlayState: StateFlow<OverlayState> = combine(
        permissionHelper.hasLocationPermission,
        dataStorePreferences.welcomeScreenShown
    ) { locationState, welcomeScreenShown ->
        Log.d("MainViewModel", "Location state: $locationState, welcome screen shown: $welcomeScreenShown")
        when {
            !welcomeScreenShown -> OverlayState.NEW_USER
            !locationState -> OverlayState.LOCATION_PERM_MISSING
            else -> OverlayState.NONE
        }
    }.stateIn(viewModelScope, SharingStarted.Companion.Eagerly, OverlayState.IDLE)

    init {
        refreshLocationPermissionState()
    }

    /**
     * Refreshes location permission state.
     */
    fun refreshLocationPermissionState() {
        permissionHelper.refreshLocationPermissionState()
    }

    /**
     * Called when welcome screen is seen and passed by user.
     */
    fun onWelcomeScreenPass() {
        viewModelScope.launch {
            dataStorePreferences.setWelcomeScreenShown(true)
        }
    }
}

enum class OverlayState {
    IDLE,
    NEW_USER,
    LOCATION_PERM_MISSING,
    NONE
}
