package com.youllbecold.logdatabase.internal.log

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.internal.log.entity.ClothesId
import com.youllbecold.logdatabase.internal.log.entity.FeelingEntity
import com.youllbecold.logdatabase.internal.log.entity.LogEntity
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Implementation of [LogRepository].
 */
internal class LogRepositoryImpl(
    private val logDao: LogDao
) : LogRepository {
    override fun getAllWithPaging(pageSize: Int): Flow<PagingData<LogData>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { logDao.getAllPaging() }
    ).flow.map { pagingData ->
        pagingData.map { logEntity -> logEntity.toModel() }
    }

    override suspend fun getLogsInRange(apparentTempRange: Pair<Double, Double>): List<LogData> =
        logDao.getAllInRange(
            apparentTempRange.first,
            apparentTempRange.second
        ).map { it.toModel() }

    override suspend fun getLog(id: Int): LogData? =
        logDao.getById(id).first()?.toModel()

    override suspend fun addLog(log: LogData) {
        logDao.insert(log.toEntity())
    }

    override suspend fun updateLog(log: LogData) {
        logDao.update(log.toEntity())
    }

    override suspend fun deleteLog(log: LogData) {
        logDao.delete(log.toEntity())
    }

    private fun LogEntity.toModel(): LogData = LogData(
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
        ClothesId.SHORT_TSHIRT_DRESS -> Clothes.SHORT_TSHIRT_DRESS
        ClothesId.BASEBALL_HAT -> Clothes.BASEBALL_HAT
        ClothesId.WINTER_HAT -> Clothes.WINTER_HAT
        ClothesId.TANK_TOP -> Clothes.TANK_TOP
        ClothesId.LIGHT_JACKET -> Clothes.LIGHT_JACKET
        ClothesId.WINTER_JACKET -> Clothes.WINTER_JACKET
        ClothesId.LEGGINGS -> Clothes.LEGGINGS
        ClothesId.WARM_PANTS -> Clothes.WARM_PANTS
        ClothesId.WINTER_SHOES -> Clothes.WINTER_SHOES
        ClothesId.LONG_TSHIRT_DRESS -> Clothes.LONG_TSHIRT_DRESS
        ClothesId.TIGHTS -> Clothes.TIGHTS
        ClothesId.SCARF -> Clothes.SCARF
        ClothesId.GLOVES -> Clothes.GLOVES
        ClothesId.SUNGLASSES -> Clothes.SUNGLASSES
        ClothesId.LONG_SKIRT -> Clothes.LONG_SKIRT
        ClothesId.SLEEVELESS_SHORT_DRESS -> Clothes.SLEEVELESS_SHORT_DRESS
        ClothesId.SLEVESLESS_LONG_DRESS -> Clothes.SLEVESLESS_LONG_DRESS
        ClothesId.LONG_SLEEVE_SHORT_DRESS -> Clothes.LONG_SLEEVE_SHORT_DRESS
        ClothesId.LONG_SLEEVE_LONG_DRESS -> Clothes.LONG_SLEEVE_LONG_DRESS
        ClothesId.HEAD_SCARF -> Clothes.HEAD_SCARF
        ClothesId.BEANIE -> Clothes.BEANIE
        ClothesId.CROP_TOP -> Clothes.CROP_TOP
        ClothesId.SHIRT -> Clothes.SHIRT
        ClothesId.SWEATER -> Clothes.SWEATER
        ClothesId.CARDIGAN -> Clothes.CARDIGAN
        ClothesId.JUMPER -> Clothes.JUMPER
        ClothesId.HOODIE -> Clothes.HOODIE
        ClothesId.JEAN_JACKET -> Clothes.JEAN_JACKET
        ClothesId.LEATHER_JACKET -> Clothes.LEATHER_JACKET
        ClothesId.WINTER_COAT -> Clothes.WINTER_COAT
        ClothesId.FLIP_FLOPS -> Clothes.FLIP_FLOPS
        ClothesId.FINGERLESS_GLOVES -> Clothes.FINGERLESS_GLOVES
        ClothesId.WINTER_GLOVES -> Clothes.WINTER_GLOVES
        ClothesId.WINTER_SCARF -> Clothes.WINTER_SCARF
    }

    private fun Clothes.toClothesId(): ClothesId = when (this) {
        Clothes.SHORT_SLEEVE -> ClothesId.SHORT_SLEEVE
        Clothes.LONG_SLEEVE -> ClothesId.LONG_SLEEVE
        Clothes.SHORT_SKIRT -> ClothesId.SHORT_SKIRT
        Clothes.SHORTS -> ClothesId.SHORTS
        Clothes.JEANS -> ClothesId.JEANS
        Clothes.SANDALS -> ClothesId.SANDALS
        Clothes.TENNIS_SHOES -> ClothesId.TENNIS_SHOES
        Clothes.SHORT_TSHIRT_DRESS -> ClothesId.SHORT_TSHIRT_DRESS
        Clothes.BASEBALL_HAT -> ClothesId.BASEBALL_HAT
        Clothes.WINTER_HAT -> ClothesId.WINTER_HAT
        Clothes.TANK_TOP -> ClothesId.TANK_TOP
        Clothes.LIGHT_JACKET -> ClothesId.LIGHT_JACKET
        Clothes.WINTER_JACKET -> ClothesId.WINTER_JACKET
        Clothes.LEGGINGS -> ClothesId.LEGGINGS
        Clothes.WARM_PANTS -> ClothesId.WARM_PANTS
        Clothes.WINTER_SHOES -> ClothesId.WINTER_SHOES
        Clothes.LONG_TSHIRT_DRESS -> ClothesId.LONG_TSHIRT_DRESS
        Clothes.TIGHTS -> ClothesId.TIGHTS
        Clothes.SCARF -> ClothesId.SCARF
        Clothes.GLOVES -> ClothesId.GLOVES
        Clothes.SUNGLASSES -> ClothesId.SUNGLASSES
        Clothes.LONG_SKIRT -> ClothesId.LONG_SKIRT
        Clothes.HEAD_SCARF -> ClothesId.HEAD_SCARF
        Clothes.BEANIE -> ClothesId.BEANIE
        Clothes.CROP_TOP -> ClothesId.CROP_TOP
        Clothes.SHIRT -> ClothesId.SHIRT
        Clothes.SWEATER -> ClothesId.SWEATER
        Clothes.CARDIGAN -> ClothesId.CARDIGAN
        Clothes.JUMPER -> ClothesId.JUMPER
        Clothes.HOODIE -> ClothesId.HOODIE
        Clothes.JEAN_JACKET -> ClothesId.JEAN_JACKET
        Clothes.LEATHER_JACKET -> ClothesId.LEATHER_JACKET
        Clothes.WINTER_COAT -> ClothesId.WINTER_COAT
        Clothes.FLIP_FLOPS -> ClothesId.FLIP_FLOPS
        Clothes.SLEVESLESS_LONG_DRESS -> ClothesId.SLEVESLESS_LONG_DRESS
        Clothes.SLEEVELESS_SHORT_DRESS -> ClothesId.SLEEVELESS_SHORT_DRESS
        Clothes.LONG_SLEEVE_LONG_DRESS -> ClothesId.LONG_SLEEVE_LONG_DRESS
        Clothes.LONG_SLEEVE_SHORT_DRESS -> ClothesId.LONG_SLEEVE_SHORT_DRESS
        Clothes.FINGERLESS_GLOVES -> ClothesId.FINGERLESS_GLOVES
        Clothes.WINTER_GLOVES -> ClothesId.WINTER_GLOVES
        Clothes.WINTER_SCARF -> ClothesId.WINTER_SCARF
    }
}
