package com.youllbecold.weather

import com.youllbecold.weather.api.WeatherRepository
import com.youllbecold.weather.internal.data.dao.WeatherDao
import com.youllbecold.weather.internal.data.repository.WeatherRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Provider for weather data.
 */
object WeatherProvider {
    private val retrofit: Retrofit by lazy { buildRetrofit() }
    private val weatherDao: WeatherDao by lazy { retrofit.create(WeatherDao::class.java) }

    /**
     * Repository for weather data.
     */
    val weatherRepository: WeatherRepository by lazy { WeatherRepositoryImpl(weatherDao) }

    private fun buildRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .build()

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
private const val CONNECTION_TIMEOUT = 10L
