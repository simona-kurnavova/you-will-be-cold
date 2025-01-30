package com.youllbecold.trustme.utils

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

/**
 * Helper class for fetching the device's location.
 */
@Singleton
class LocationHelper {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var _locationState: MutableStateFlow<LocationState> = MutableStateFlow(LocationState.Idle)
    val locationState: StateFlow<LocationState> = _locationState

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun refreshLocation(context: Context) {
        _locationState.value = LocationState.Loading

        coroutineScope.launch {
            val locationClient = LocationServices.getFusedLocationProviderClient(context)
            val priority = Priority.PRIORITY_HIGH_ACCURACY

            locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                .addOnCompleteListener { task: Task<android.location.Location> ->
                    if (task.isSuccessful && task.result != null) {
                        val location = task.result
                        _locationState.value = LocationState.Success(Location(location.latitude, location.longitude))
                    } else {
                        task.exception?.let {
                            _locationState.value = LocationState.Error(it.message ?: UNKNOWN_ERROR_MESSAGE)
                        }
                    }
            }
        }
    }
}

data class Location(
    val latitude: Double,
    val longitude: Double
)

sealed class LocationState {
    data object Idle : LocationState()
    data object Loading : LocationState()
    data class Error(val message: String) : LocationState()
    data class Success(val location: Location) : LocationState()
}

private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
