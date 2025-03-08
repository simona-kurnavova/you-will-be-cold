package com.youllbecold.logdatabase

import android.app.Application
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.internal.data.database.LogDatabase
import com.youllbecold.logdatabase.internal.data.repository.LogRepositoryImpl

/**
 * Provider for LogRepository.
 */
object LogRepositoryProvider {
    /**
     * Provides the [LogRepository].
     *
     * @param application The application.
     */
    fun repository(application: Application): LogRepository =
        LogRepositoryImpl(LogDatabase.getInstance(application).logDao())
}
