package com.youllbecold.trustme.common.data.di

import android.app.NotificationManager
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.youllbecold.logdatabase.LogRepositoryProvider
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.recomendation.RecommendRepositoryProvider
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.trustme.common.data.preferences.DataStorePreferences
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.LocationPermissionManager
import com.youllbecold.weather.WeatherProvider
import com.youllbecold.weather.api.WeatherRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dataModule = module {
    // Repositories (from different modules)
    factory<LogRepository> { LogRepositoryProvider.repository(androidApplication()) }
    factory<RecommendRepository> { RecommendRepositoryProvider.repository(androidApplication()) }
    factory<WeatherRepository> { WeatherProvider.weatherRepository }

    // System Services
    single<NotificationManager> {
        androidApplication().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidApplication())
    }

    // Location
    singleOf(::LocationController)

    // Network
    singleOf(::NetworkStatusProvider)

    // Permissions
    singleOf(::LocationPermissionManager)

    // Preferences
    singleOf(::DataStorePreferences)
}
