package com.youllbecold.trustme.common.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Controller class for fetching device's location and city.
 */
@OptIn(ExperimentalAtomicApi::class)
@Singleton
class LocationController(
    private val app: Application,
    private val locationClient: FusedLocationProviderClient,
    permissionManager: LocationPermissionManager,
) {
    private val dispatcher = Dispatchers.IO
    private val coroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    private var refreshRunning = AtomicReference<Boolean>(false)

    private val cachedLocation: MutableStateFlow<GeoLocation?> = MutableStateFlow<GeoLocation?>(null)
    private val cachedLocationWithCity: MutableStateFlow<GeoLocationWithCity?> = MutableStateFlow(null)

    init {
        // Refresh location when permission is granted
        permissionManager.hasLocationPermission
            .onEach { hasPermission ->
                if (hasPermission) {
                    refreshLocation()
                }
            }.launchIn(coroutineScope)

        // Update city when location changes
        cachedLocation
            .onEach { location ->
                if (location != null && cachedLocationWithCity.value?.geoLocation != location) {
                    updateAddress(location)
                }
            }.launchIn(coroutineScope)
    }

    /**
     * Refreshes the device's location with location permission check.
     */
    @SuppressLint("MissingPermission") // Not missing, handled by the permission helper.
    fun refreshLocation() {
        if (!PermissionChecker.hasLocationPermission(app)) {
            return
        }

        if (refreshRunning.compareAndExchange(expectedValue = false, newValue = true)) {
            return // Return if already running
        }

        coroutineScope.launch {
            val location = getLastLocation() ?: return@launch
            cachedLocation.update { location }

            refreshRunning.store(false)
        }
    }

    /**
     * Returns location as quickly as possible - checks cached one, if not available fetches it.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun quickGetLastLocation(): GeoLocation? =  withContext(dispatcher) {
        cachedLocation.firstOrNull() ?: getLastLocation()
    }

    /**
     * Returns city for coordinates - first checks cache then fetches it.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */
    suspend fun quickGetCity(latitude: Double, longitude: Double): String? =
        withContext(dispatcher) {
            cachedLocationWithCity.firstOrNull()
                ?.takeIf { it.geoLocation.latitude == latitude && it.geoLocation.longitude == longitude }
                ?.city
                ?: fetchCity(latitude, longitude)
                    .also { city ->
                        // Cache it
                        cachedLocationWithCity.update {
                            GeoLocationWithCity(GeoLocation(latitude, longitude), city)
                        }
                    }
        }

    /**
     * One-time location request. Used for obtaining one-time synchronous location (without city).
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private suspend fun getLastLocation(): GeoLocation? =
        suspendCancellableCoroutine { continuation ->
            locationClient.lastLocation
                .addOnSuccessListener {
                    continuation.resume(it?.let { GeoLocation(it.latitude, it.longitude) })
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }.also { location ->
            cachedLocation.update { location }
        }

    /**
     * One-time city request.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */
    private suspend fun fetchCity(latitude: Double, longitude: Double): String? =
        suspendCoroutine { continuation ->
            AddressProvider.obtainAddress(app, latitude, longitude) { address ->
                val city = address?.locality ?: address?.adminArea ?: address?.countryName
                continuation.resume(city)
            }
        }

    /**
     * Updates the city for the given location.
     */
    private fun updateAddress(geoLocation: GeoLocation) {
        AddressProvider.obtainAddress(app, geoLocation.latitude, geoLocation.longitude) { address ->
            val city = address?.locality ?: address?.adminArea ?: address?.countryName

            cachedLocationWithCity.update {
                GeoLocationWithCity(geoLocation, city)
            }
        }
    }
}

/**
 * Data class representing a geo location.
 *
 * @param latitude The latitude of the location.
 * @param longitude The longitude of the location.
 */
data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
)

/**
 * Data class representing a geo location with city.
 *
 * @param geoLocation The geo location.
 * @param city The city name (if available).
 */
data class GeoLocationWithCity(
    val geoLocation: GeoLocation,
    val city: String?,
)
