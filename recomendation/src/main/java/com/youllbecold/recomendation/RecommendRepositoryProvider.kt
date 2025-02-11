package com.youllbecold.recomendation

import android.app.Application
import com.youllbecold.logdatabase.LogRepositoryProvider
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.recomendation.internal.RecommendRepositoryImpl

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
        RecommendRepositoryImpl(LogRepositoryProvider.repository(application))
}
