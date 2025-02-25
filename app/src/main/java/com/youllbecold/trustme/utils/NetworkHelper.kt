package com.youllbecold.trustme.utils

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Singleton

@Singleton
class NetworkHelper(private val app: Application) {
    private val connectivityManager by lazy {
        app.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isConnected.value =
                connectivityManager.getNetworkCapabilities(network)?.hasInternet() == true
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            _isConnected.value = networkCapabilities.hasInternet()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isConnected.value = false
        }
    }

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * StateFlow that emits the current internet connection status.
     */
    val isConnected: StateFlow<Boolean> = _isConnected

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Check initial network status
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        _isConnected.value = networkCapabilities?.hasInternet() ?: false
    }

    /**
     * Check if the device has internet connection.
     */
    fun hasInternet(): Boolean = _isConnected.value

    private fun NetworkCapabilities.hasInternet(): Boolean =
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

