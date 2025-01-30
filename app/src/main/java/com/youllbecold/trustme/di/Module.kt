package com.youllbecold.trustme.di

import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.SettingsViewModel
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.weatherservice.WeatherApi
import com.youllbecold.trustme.weatherservice.WeatherProvider
import com.youllbecold.trustme.weatherservice.WeatherRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val appModule = module {
    single<Retrofit> { buildRetrofit() }
    single<DataStorePreferences> { DataStorePreferences(androidApplication()) }
    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }
    single<WeatherRepository> { WeatherRepository(get()) }
    single<WeatherProvider> { WeatherProvider(androidApplication(), get(), get()) }
    single<LocationHelper> { LocationHelper() }
}

val uiModule = module {
    viewModel<SettingsViewModel> { SettingsViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
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
