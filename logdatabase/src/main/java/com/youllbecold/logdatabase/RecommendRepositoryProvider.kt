package com.youllbecold.logdatabase

import android.app.Application
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.api.RecommendRepository
import com.youllbecold.logdatabase.internal.log.LogDatabase
import com.youllbecold.logdatabase.internal.recommendation.RecommendRepositoryImpl

/**
 * Provider for LogRepository.
 */
object RecommendRepositoryProvider {
    /**
     * Provides the [LogRepository].
     *
     * @param application The application.
     */
    fun repository(application: Application): RecommendRepository =
        RecommendRepositoryImpl(LogDatabase.getInstance(application).logDao())
}