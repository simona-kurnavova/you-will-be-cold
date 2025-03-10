package com.youllbecold.trustme.common.domain.units

import com.youllbecold.trustme.common.data.preferences.DataStorePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Manager for units settings.
 */
class UnitsManager(
    private val dataStorePreferences: DataStorePreferences,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Whether to use celsius units.
     */
    val unitsCelsius: Flow<Boolean> by dataStorePreferences::useCelsiusUnits

    /**
     * Set whether to use celsius units.
     */
    fun setUnitsCelsius(unitsCelsius: Boolean) {
        coroutineScope.launch {
            if (this@UnitsManager.unitsCelsius.first() != unitsCelsius) {
                dataStorePreferences.setUseCelsiusUnits(unitsCelsius)
            }
        }
    }
}
