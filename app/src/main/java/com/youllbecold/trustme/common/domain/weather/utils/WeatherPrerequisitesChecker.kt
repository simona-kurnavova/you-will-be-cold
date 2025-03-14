package com.youllbecold.trustme.common.domain.weather.utils

import android.app.Application
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Utility class for checking the prerequisites for a use case.
 */
object WeatherPrerequisitesChecker : KoinComponent {

    private val app by lazy { get<Application>() }
    private val networkStatusProvider by lazy { get<NetworkStatusProvider>() }

    /**
     * Checks the prerequisites for the use case.
     */
    fun check(): Status {
        when {
            !PermissionChecker.hasLocationPermission(app) ->
                return Error.MissingPermission
            !networkStatusProvider.hasInternet() ->
                return Error.NoInternet
        }
        return Success
    }
}
