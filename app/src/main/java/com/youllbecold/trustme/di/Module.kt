package com.youllbecold.trustme.di

import com.youllbecold.logdatabase.LogRepositoryProvider
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.ui.viewmodels.HistoryViewModel
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.MainViewModel
import com.youllbecold.trustme.ui.viewmodels.SettingsViewModel
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
import com.youllbecold.trustme.weatherservice.internal.WeatherApi
import com.youllbecold.trustme.weatherservice.WeatherProvider
import com.youllbecold.trustme.weatherservice.internal.WeatherRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    singleOf(::DataStorePreferences)

    // Helpers
    singleOf(::LocationHelper)
    singleOf(::PermissionHelper)

    // Log Repository
    single<LogRepository> { LogRepositoryProvider.repository(androidApplication()) }

    // Weather API
    single<Retrofit> { buildRetrofit() }
    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }
    singleOf(::WeatherRepository)
    singleOf(::WeatherProvider)
}

val uiModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::MainViewModel)
}

private fun buildRetrofit(): Retrofit {
    val client = OkHttpClient()
    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val clientBuilder = client.newBuilder().addInterceptor(interceptor)

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(clientBuilder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private const val BASE_URL = "https://api.open-meteo.com"

