package com.youllbecold.trustme.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.youllbecold.trustme.ui.viewmodels.state.LoadingStatus
import com.youllbecold.trustme.ui.viewmodels.state.isIdle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Helper class for fetching the device's location.
 */
@SuppressLint("MissingPermission") // Not missing, handled by the permission helper.
@Singleton
class LocationHelper(
    private val app: Application,
    permissionHelper: PermissionHelper,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val locationClient by lazy { LocationServices.getFusedLocationProviderClient(app) }

    private val _geoLocation: MutableStateFlow<GeoLocationState> = MutableStateFlow(GeoLocationState()).apply {
        onEach { state ->
            // Update the city if the location changes.
            if (state.status.isIdle() && state.city == null) {
                state.location?.let { updateAddress(it) }
            }
        }.launchIn(coroutineScope)
    }

    /**
     * State flow for the device's location.
     */
    val geoLocationState: StateFlow<GeoLocationState> = _geoLocation

    init {
        coroutineScope.launch {
            permissionHelper.hasLocationPermission.collectLatest { hasPermission ->
                if (hasPermission) {
                    refresh()
                }
            }
        }
    }

    /**
     * Refreshes the device's location.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun refresh() {
        if (!PermissionHelper.hasLocationPermission(app)) {
            return // Sanity check
        }

        _geoLocation.update { it.copy(status = LoadingStatus.Loading) }

        locationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                _geoLocation.update {
                    GeoLocationState(GeoLocation(task.result.latitude, task.result.longitude))
                }
            } else {
                _geoLocation.update { it.copy(status = LoadingStatus.GenericError) }
            }
        }
    }

    private fun updateAddress(geoLocation: GeoLocation) {
        obtainAddress(app, geoLocation.latitude, geoLocation.longitude) { address ->
            val city = address?.locality ?: address?.adminArea ?: address?.countryName

            _geoLocation.update {
                // Consistency check, if the location is still the same, update the city.
                if (it.location == geoLocation) {
                    it.copy(city = city)
                } else {
                    it
                }
            }
        }
    }

    private fun obtainAddress(
        context: Context,
        latitude: Double,
        longitude: Double,
        onResult: (Address?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getAddressApi33AndHigher(context, latitude, longitude) { city -> onResult(city) }
        } else {
            coroutineScope.launch {
                // Blocking call, can linger for a while on lower API levels
                val city = getAddress(context, latitude, longitude)
                onResult(city)
            }
        }
    }

    @Suppress("Deprecation") // Geocoder.getFromLocation is deprecated, but needed for lower Androids.
    private suspend fun getAddress(
        context: Context,
        latitude: Double,
        longitude: Double
    ): Address? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getAddressApi33AndHigher(
        context: Context,
        latitude: Double,
        longitude: Double,
        action: (Address?) -> Unit
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val listener = GeocodeListener { addresses ->
            action(addresses.firstOrNull())
        }
        geocoder.getFromLocation(latitude, longitude, 1, listener)
    }

    companion object {
        /**
         * One-time location request.
         */
        @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        suspend fun getLastLocation(context: Context): GeoLocation? =
            withContext(Dispatchers.IO) {
                suspendCoroutine { continuation ->
                    val locClient = LocationServices.getFusedLocationProviderClient(context)
                    locClient.lastLocation
                        .addOnSuccessListener {
                            if (it != null) {
                                continuation.resume(GeoLocation(it.latitude, it.longitude))
                            } else {
                                continuation.resume(null)
                            }
                        }.addOnFailureListener {
                            continuation.resume(null)
                        }
                    }
                }
            }
}

data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
)

data class GeoLocationState(
    val location: GeoLocation? = null,
    val status: LoadingStatus = LoadingStatus.Idle,
    val city: String? = null
)
