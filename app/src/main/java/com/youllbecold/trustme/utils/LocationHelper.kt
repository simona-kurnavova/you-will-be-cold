package com.youllbecold.trustme.utils

import android.Manifest
import android.content.Context
import android.location.Address
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Helper class for fetching the device's location.
 */
object LocationHelper {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun refreshLocation(context: Context, onSuccess: (Location) -> Unit, onError: (String?) -> Unit) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val priority = Priority.PRIORITY_HIGH_ACCURACY

        locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
            .addOnCompleteListener { task: Task<android.location.Location> ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result

                    obtainAddress(context, location.latitude, location.longitude) { address ->
                        // City might be null, but that's fine.
                        val city = address?.locality ?: address?.adminArea ?: address?.countryName
                        onSuccess(Location(location.latitude, location.longitude, city))
                    }

                } else {
                    task.exception?.let {
                        onError(it.message)
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

/**
 * Data class representing a location.
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val city: String? = null
)
