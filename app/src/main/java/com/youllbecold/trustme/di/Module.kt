package com.youllbecold.trustme.di

import com.youllbecold.logdatabase.LogRepositoryProvider
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.ui.viewmodels.HistoryViewModel
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.MainViewModel
import com.youllbecold.trustme.ui.viewmodels.SettingsViewModel
import com.youllbecold.trustme.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.usecases.weather.HourlyWeatherUseCase
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.trustme.utils.NetworkHelper
import com.youllbecold.weather.WeatherProvider
import com.youllbecold.weather.api.WeatherRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DataStorePreferences)

    // Helpers
    singleOf(::PermissionHelper)
    singleOf(::NetworkHelper)

    // Repositories
    single<LogRepository> { LogRepositoryProvider.repository(androidApplication()) }
    single<WeatherRepository> { WeatherProvider.weatherRepository }
    
    // UseCases
    singleOf(::CurrentWeatherUseCase)
    singleOf(::HourlyWeatherUseCase)
}

val uiModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::MainViewModel)
}

