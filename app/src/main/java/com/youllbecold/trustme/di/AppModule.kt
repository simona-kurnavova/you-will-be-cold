package com.youllbecold.trustme.di

import com.youllbecold.logdatabase.LogRepositoryProvider
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.recomendation.RecommendRepositoryProvider
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.trustme.notifications.NotificationHelper
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.usecases.recommendation.RecommendationUseCase
import com.youllbecold.trustme.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.usecases.weather.HourlyWeatherUseCase
import com.youllbecold.trustme.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.NetworkHelper
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.weather.WeatherProvider
import com.youllbecold.weather.api.WeatherRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val appModule = module {
    // Preferences
    singleOf(::DataStorePreferences)

    // Helpers
    singleOf(::PermissionHelper)
    singleOf(::NetworkHelper)
    singleOf(::LocationHelper)
    singleOf(::NotificationHelper)
    
    // UseCases
    factoryOf(::CurrentWeatherUseCase)
    factoryOf(::HourlyWeatherUseCase)
    factoryOf(::RangedWeatherUseCase)
    factoryOf(::RecommendationUseCase)

    // Repositories (from different modules)
    single<LogRepository> { LogRepositoryProvider.repository(androidApplication()) }
    single<RecommendRepository> { RecommendRepositoryProvider.repository(androidApplication()) }
    single<WeatherRepository> { WeatherProvider.weatherRepository }
}
