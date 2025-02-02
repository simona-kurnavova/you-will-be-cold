package com.youllbecold.trustme.di

import com.youllbecold.trustme.preferences.DataStorePreferences
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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<Retrofit> { buildRetrofit() }
    single<DataStorePreferences> { DataStorePreferences(androidApplication()) }
    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }
    single<WeatherRepository> { WeatherRepository(get()) }
    single<WeatherProvider> { WeatherProvider(androidApplication(), get(), get(), get()) }

    // Helpers
    single<LocationHelper> { LocationHelper() }
    single<PermissionHelper> { PermissionHelper(androidApplication())  }
}

val uiModule = module {
    viewModel<SettingsViewModel> { SettingsViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get(), get()) }
    viewModel<MainViewModel> { MainViewModel(get(), get()) }
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
