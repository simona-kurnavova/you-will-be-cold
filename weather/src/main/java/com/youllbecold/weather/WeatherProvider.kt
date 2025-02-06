package com.youllbecold.weather

import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.internal.WeatherApi
import com.youllbecold.weather.internal.WeatherRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provider for weather data.
 */
object WeatherProvider {
    private val retrofit: Retrofit by lazy { buildRetrofit() }
    private val weatherApi: WeatherApi by lazy { retrofit.create(WeatherApi::class.java) }

    /**
     * Repository for weather data.
     */
    val weatherRepository: WeatherRepository by lazy { WeatherRepositoryImpl(weatherApi) }

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
}

private const val BASE_URL = "https://api.open-meteo.com"
