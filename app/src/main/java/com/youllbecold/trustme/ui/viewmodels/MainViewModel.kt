package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.youllbecold.trustme.utils.PermissionHelper
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val permissionHelper: PermissionHelper
) : ViewModel() {

    /**
     * Flow of the current location permission state.
     */
    val locationGranted: StateFlow<Boolean> = permissionHelper.locationState

    init {
        refreshLocationPermissionState()
    }

    fun refreshLocationPermissionState() {
        permissionHelper.refreshLocationPermissionState()
    }
}
