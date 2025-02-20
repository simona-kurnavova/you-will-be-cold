package com.youllbecold.trustme.utils

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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.youllbecold.trustme.ui.viewmodels.LoadingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import java.util.Locale

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

    private val _geoLocation: MutableStateFlow<GeoLocationState> = MutableStateFlow(GeoLocationState())

    /**
     * State flow for the device's location.
     */
    val geoLocationState: StateFlow<GeoLocationState> = _geoLocation

    init {
        coroutineScope.launch {
            val hasPermission = permissionHelper.hasLocationPermission.first()

            if (hasPermission) {
                refresh()
            }

            // Update address when location changes.
            _geoLocation.collectLatest { geo ->
                geo.location?.let { updateAddress(it) }
            }
        }
    }

    /**
     * Refreshes the device's location.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun refresh() {
        _geoLocation.update { it.copy(status = LoadingStatus.Loading) }

        locationClient.lastLocation.addOnCompleteListener { task: Task<android.location.Location> ->
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

    private fun obtainAddress(context: Context, latitude: Double, longitude: Double, onResult: (Address?) -> Unit) {
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
    private suspend fun getAddress(context: Context, latitude: Double, longitude: Double): Address? =
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
    private fun getAddressApi33AndHigher(context: Context, latitude: Double, longitude: Double, action: (Address?) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val listener = GeocodeListener { addresses ->
            action(addresses.firstOrNull())
        }
        geocoder.getFromLocation(latitude, longitude, 1, listener)
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
