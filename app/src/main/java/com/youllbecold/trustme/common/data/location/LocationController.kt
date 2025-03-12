package com.youllbecold.trustme.common.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
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
 * Controller class for fetching device's location and city.
 */
@Singleton
class LocationController(
    private val app: Application,
    private val locationClient: FusedLocationProviderClient,
    permissionManager: LocationPermissionManager,
) {
    private val dispatcher = Dispatchers.IO
    private val coroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    private val cachedGeoLocation: MutableStateFlow<GeoLocation?> = MutableStateFlow<GeoLocation?>(null).apply {
        onEach { location ->
            if (location != null) {
                updateAddress(location)
            }
        }.launchIn(coroutineScope)
    }

    private val cachedLocationWithCity: MutableStateFlow<GeoLocationWithCity?> = MutableStateFlow(null)

    private var refreshRunning: Boolean = false

    init {
        coroutineScope.launch {
            permissionManager.hasLocationPermission.collectLatest { hasPermission ->
                if (hasPermission) {
                    refreshLocation()
                }
            }
        }
    }

    /**
     * Refreshes the device's location.
     */
    @SuppressLint("MissingPermission") // Not missing, handled by the permission helper.
    fun refreshLocation() {
        if (!PermissionChecker.hasLocationPermission(app) || refreshRunning) {
            return
        }

        refreshRunning = true

        coroutineScope.launch {
            val location = getLastLocation() ?: return@launch
            cachedGeoLocation.update { location }

            refreshRunning = false
        }
    }

    /**
     * Returns location as quickly as possible - checks cached one, if not available fetches it.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun quickGetLastLocation(): GeoLocation? =  withContext(dispatcher) {
        cachedGeoLocation.firstOrNull() ?: getLastLocation()
    }

    /**
     * Returns city for coordinates - first checks cache then fetches it.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */
    suspend fun quickGetCity(latitude: Double, longitude: Double): String? =
        withContext(dispatcher) {
            val cached = cachedLocationWithCity.firstOrNull()
            val loc = cached?.geoLocation

            if (loc?.latitude == latitude && loc.longitude == longitude) {
                return@withContext cached.city
            }

            return@withContext fetchCity(latitude, longitude)
        }

    /**
     * One-time location request. Used for obtaining one-time synchronous location (without city).
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private suspend fun getLastLocation(): GeoLocation? =
        suspendCoroutine { continuation ->
            locationClient.lastLocation
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

    /**
     * One-time city request.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */
    private suspend fun fetchCity(latitude: Double, longitude: Double): String? =
        suspendCoroutine { continuation ->
            obtainAddress(app, latitude, longitude) { address ->
                val city = address?.locality ?: address?.adminArea ?: address?.countryName
                continuation.resume(city)
            }
        }

    private fun updateAddress(geoLocation: GeoLocation) {
        obtainAddress(app, geoLocation.latitude, geoLocation.longitude) { address ->
            val city = address?.locality ?: address?.adminArea ?: address?.countryName

            cachedLocationWithCity.update {
                GeoLocationWithCity(geoLocation, city)
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
    private fun getAddress(
        context: Context,
        latitude: Double,
        longitude: Double
    ): Address? = try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        addresses?.firstOrNull()
    } catch (e: Exception) {
        null
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
