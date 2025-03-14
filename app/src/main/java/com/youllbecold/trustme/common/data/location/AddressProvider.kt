package com.youllbecold.trustme.common.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Provides address for the location.
 */
object AddressProvider {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Obtains the address for the given latitude and longitude.
     *
     * @param context The context.
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @param onResult The callback with the address.
     */
    fun obtainAddress(
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
