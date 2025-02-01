package com.youllbecold.trustme.utils

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import java.util.Locale

/**
 * Helper class for fetching the device's location.
 */
@Singleton
class LocationHelper {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var _locationState: MutableStateFlow<LocationState> = MutableStateFlow(LocationState.Idle)

    /**
     * Flow of the current location state.
     */
    val locationState: StateFlow<LocationState> = _locationState

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun refreshLocation(context: Context) {
        _locationState.value = LocationState.Loading

        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val priority = Priority.PRIORITY_HIGH_ACCURACY

        locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
            .addOnCompleteListener { task: Task<android.location.Location> ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result

                    obtainCity(context, location.latitude, location.longitude) { city ->
                        // City might be null, but that's fine.
                        _locationState.value = LocationState.Success(
                            Location(location.latitude, location.longitude, city)
                        )
                    }

                } else {
                    task.exception?.let {
                        _locationState.value = LocationState.Error(it.message)
                    }
                }
            }
    }

    private fun obtainCity(context: Context, latitude: Double, longitude: Double, onResult: (String?) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getCityApi33AndHigher(context, latitude, longitude) { city -> onResult(city) }
        } else {
            coroutineScope.launch {
                // Blocking call, can linger for a while on lower API levels
                val city = getCity(context, latitude, longitude)
                onResult(city)
            }
        }
    }

    private suspend fun getCity(context: Context, latitude: Double, longitude: Double): String? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()?.locality
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCityApi33AndHigher(context: Context, latitude: Double, longitude: Double, action: (String?) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val listener = GeocodeListener { addresses ->
            action(addresses.firstOrNull()?.locality)
        }
        geocoder.getFromLocation(latitude, longitude, 1, listener)
    }
}

/**
 * Data class representing a location.
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val city: String? = null
)

/**
 * Sealed class representing the state of the location fetching process.
 */
sealed class LocationState {
    /**
     * The location fetching process is idle.
     */
    data object Idle : LocationState()

    /**
     * The location fetching process is in progress.
     */
    data object Loading : LocationState()

    /**
     * An error occurred during the location fetching process.
     *
     * @param message The error message.
     */
    data class Error(val message: String?) : LocationState()

    /**
     * The location fetching process was successful.
     *
     * @param location The fetched location.
     */
    data class Success(val location: Location) : LocationState()
}
