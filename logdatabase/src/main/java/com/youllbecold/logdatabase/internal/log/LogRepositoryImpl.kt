package com.youllbecold.logdatabase.internal.log

import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.internal.log.entity.ClothesId
import com.youllbecold.logdatabase.internal.log.entity.FeelingEntity
import com.youllbecold.logdatabase.internal.log.entity.LogEntity
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class LogRepositoryImpl(
    private val logDao: LogDao
) : LogRepository {
    private val dispatchers = Dispatchers.IO

    override val logs: Flow<List<LogData>>
        get() = logDao.getAll().map { logs ->
            logs.map { it.toModel() }
        }

    override suspend fun getLogsInRange(apparentTempRange: Pair<Double, Double>): List<LogData> {
        return withContext(dispatchers) {
            logDao.getAllInRange(
                apparentTempRange.first,
                apparentTempRange.second
            ).map { it.toModel() }
        }
    }

    override suspend fun getLog(id: Int): LogData? {
        return withContext(dispatchers) {
            logDao.getById(id).first()?.toModel()
        }
    }

    override suspend fun addLog(log: LogData) {
        withContext(dispatchers) {
            val entity = log.toEntity()
            logDao.insert(log.toEntity())
        }
    }

    override suspend fun updateLog(log: LogData) {
        withContext(dispatchers) {
            logDao.update(log.toEntity())
        }
    }

    override suspend fun deleteLog(log: LogData) {
        withContext(dispatchers) {
            logDao.delete(log.toEntity())
        }
    }

    private fun LogEntity.toModel() = LogData(
        id = id,
        dateFrom = dateFrom,
        dateTo = dateTo,
        weatherData = WeatherData(
            apparentTemperatureMinC = apparentTemperatureMinC,
            apparentTemperatureMaxC = apparentTemperatureMaxC,
            avgTemperatureC = avgTemperatureC
        ),
        feelings = Feelings(
            head = headFeeling.toLogFeeling(),
            neck = neckFeeling.toLogFeeling(),
            top = topFeeling.toLogFeeling(),
            bottom = bottomFeeling.toLogFeeling(),
            feet = feetFeeling.toLogFeeling(),
            hand = handFeeling.toLogFeeling()
        ),
        clothes = clothes.map { it.toClothes() }
    )

    private fun LogData.toEntity(): LogEntity = LogEntity(
        id = id,
        dateFrom = dateFrom,
        dateTo = dateTo,
        avgTemperatureC = weatherData.avgTemperatureC,
        apparentTemperatureMinC = weatherData.apparentTemperatureMinC,
        apparentTemperatureMaxC = weatherData.apparentTemperatureMaxC,
        headFeeling = feelings.head.toFeelingEntity(),
        neckFeeling = feelings.neck.toFeelingEntity(),
        topFeeling = feelings.top.toFeelingEntity(),
        bottomFeeling = feelings.bottom.toFeelingEntity(),
        feetFeeling = feelings.feet.toFeelingEntity(),
        handFeeling = feelings.hand.toFeelingEntity(),
        clothes = clothes.map { it.toClothesId() },
    )

    private fun Feeling.toFeelingEntity(): FeelingEntity {
        return when (this) {
            Feeling.NORMAL -> FeelingEntity.NORMAL
            Feeling.COLD -> FeelingEntity.COLD
            Feeling.VERY_COLD -> FeelingEntity.VERY_COLD
            Feeling.WARM -> FeelingEntity.WARM
            Feeling.VERY_WARM -> FeelingEntity.VERY_WARM
        }
    }

    private fun FeelingEntity.toLogFeeling(): Feeling {
        return when (this) {
            FeelingEntity.NORMAL -> Feeling.NORMAL
            FeelingEntity.COLD -> Feeling.COLD
            FeelingEntity.VERY_COLD -> Feeling.VERY_COLD
            FeelingEntity.WARM -> Feeling.WARM
            FeelingEntity.VERY_WARM -> Feeling.VERY_WARM
        }
    }
    private fun ClothesId.toClothes(): Clothes = when (this) {
        ClothesId.SHORT_SLEEVE -> Clothes.SHORT_SLEEVE
        ClothesId.LONG_SLEEVE -> Clothes.LONG_SLEEVE
        ClothesId.SHORT_SKIRT -> Clothes.SHORT_SKIRT
        ClothesId.SHORTS -> Clothes.SHORTS
        ClothesId.JEANS -> Clothes.JEANS
        ClothesId.SANDALS -> Clothes.SANDALS
        ClothesId.TENNIS_SHOES -> Clothes.TENNIS_SHOES
        ClothesId.SHORT_DRESS -> Clothes.SHORT_DRESS
        ClothesId.BASEBALL_HAT -> Clothes.BASEBALL_HAT
        ClothesId.WINTER_HAT -> Clothes.WINTER_HAT
        ClothesId.TANK_TOP -> Clothes.TANK_TOP
        ClothesId.LIGHT_JACKET -> Clothes.LIGHT_JACKET
        ClothesId.WINTER_JACKET -> Clothes.WINTER_JACKET
        ClothesId.LEGGINGS -> Clothes.LEGGINGS
        ClothesId.WARM_PANTS -> Clothes.WARM_PANTS
        ClothesId.WINTER_SHOES -> Clothes.WINTER_SHOES
        ClothesId.LONG_DRESS -> Clothes.LONG_DRESS
        ClothesId.TIGHTS -> Clothes.TIGHTS
        ClothesId.SCARF -> Clothes.SCARF
        ClothesId.GLOVES -> Clothes.GLOVES
        ClothesId.SUNGLASSES -> Clothes.SUNGLASSES
        ClothesId.LONG_SKIRT -> Clothes.LONG_SKIRT
    }

    private fun Clothes.toClothesId(): ClothesId = when (this) {
        Clothes.SHORT_SLEEVE -> ClothesId.SHORT_SLEEVE
        Clothes.LONG_SLEEVE -> ClothesId.LONG_SLEEVE
        Clothes.SHORT_SKIRT -> ClothesId.SHORT_SKIRT
        Clothes.SHORTS -> ClothesId.SHORTS
        Clothes.JEANS -> ClothesId.JEANS
        Clothes.SANDALS -> ClothesId.SANDALS
        Clothes.TENNIS_SHOES -> ClothesId.TENNIS_SHOES
        Clothes.SHORT_DRESS -> ClothesId.SHORT_DRESS
        Clothes.BASEBALL_HAT -> ClothesId.BASEBALL_HAT
        Clothes.WINTER_HAT -> ClothesId.WINTER_HAT
        Clothes.TANK_TOP -> ClothesId.TANK_TOP
        Clothes.LIGHT_JACKET -> ClothesId.LIGHT_JACKET
        Clothes.WINTER_JACKET -> ClothesId.WINTER_JACKET
        Clothes.LEGGINGS -> ClothesId.LEGGINGS
        Clothes.WARM_PANTS -> ClothesId.WARM_PANTS
        Clothes.WINTER_SHOES -> ClothesId.WINTER_SHOES
        Clothes.LONG_DRESS -> ClothesId.LONG_DRESS
        Clothes.TIGHTS -> ClothesId.TIGHTS
        Clothes.SCARF -> ClothesId.SCARF
        Clothes.GLOVES -> ClothesId.GLOVES
        Clothes.SUNGLASSES -> ClothesId.SUNGLASSES
        Clothes.LONG_SKIRT -> ClothesId.LONG_SKIRT
    }
}
