package com.youllbecold.logdatabase.internal

import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.internal.entity.LogEntity
import com.youllbecold.logdatabase.model.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class LogRepositoryImpl(
    private val logDao: LogDao
) : LogRepository {
    private val dispatchers = Dispatchers.IO

    override val logs = logDao.getAll().map { logs ->
        logs.map { it.toModel() }
    }

    override suspend fun addLog(log: Log) {
        withContext(dispatchers) {
            logDao.insert(log.toEntity())
        }
    }

    override suspend fun updateLog(log: Log) {
        withContext(dispatchers) {
            logDao.update(log.toEntity())
        }
    }

    override suspend fun deleteLog(log: Log) {
        withContext(dispatchers) {
            logDao.delete(log.toEntity())
        }
    }

    private fun LogEntity.toModel() = Log(
        id = id,
        date = date,
        overallFeeling = Log.Feeling.VERY_COLD,
        weatherData = Log.WeatherData(
            temperature = temperature,
            apparentTemperature = apparentTemperature
        )
    )

    private fun Log.toEntity(): LogEntity = LogEntity(
        date = date,
        //overallFeeling = overallFeeling.ordinal.toDouble(),
        temperature = weatherData.temperature,
        apparentTemperature = weatherData.apparentTemperature
    )
}

