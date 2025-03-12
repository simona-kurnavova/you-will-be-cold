package com.youllbecold.logdatabase.internal.data.mappers

import com.youllbecold.logdatabase.internal.data.model.ClothesId
import com.youllbecold.logdatabase.internal.data.model.FeelingEntity
import com.youllbecold.logdatabase.internal.data.model.LogEntity
import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.LogData

/**
 * Maps [LogData] to [LogEntity].
 */
internal fun LogData.toLogEntity(): LogEntity = LogEntity(
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

private fun ClothesModel.toClothesId(): ClothesId = when (this) {
    ClothesModel.SHORT_SLEEVE -> ClothesId.SHORT_SLEEVE
    ClothesModel.LONG_SLEEVE -> ClothesId.LONG_SLEEVE
    ClothesModel.SHORT_SKIRT -> ClothesId.SHORT_SKIRT
    ClothesModel.SHORTS -> ClothesId.SHORTS
    ClothesModel.JEANS -> ClothesId.JEANS
    ClothesModel.SANDALS -> ClothesId.SANDALS
    ClothesModel.TENNIS_SHOES -> ClothesId.TENNIS_SHOES
    ClothesModel.SHORT_TSHIRT_DRESS -> ClothesId.SHORT_TSHIRT_DRESS
    ClothesModel.BASEBALL_HAT -> ClothesId.BASEBALL_HAT
    ClothesModel.WINTER_HAT -> ClothesId.WINTER_HAT
    ClothesModel.TANK_TOP -> ClothesId.TANK_TOP
    ClothesModel.LIGHT_JACKET -> ClothesId.LIGHT_JACKET
    ClothesModel.WINTER_JACKET -> ClothesId.WINTER_JACKET
    ClothesModel.LEGGINGS -> ClothesId.LEGGINGS
    ClothesModel.WARM_PANTS -> ClothesId.WARM_PANTS
    ClothesModel.WINTER_SHOES -> ClothesId.WINTER_SHOES
    ClothesModel.LONG_TSHIRT_DRESS -> ClothesId.LONG_TSHIRT_DRESS
    ClothesModel.TIGHTS -> ClothesId.TIGHTS
    ClothesModel.SCARF -> ClothesId.SCARF
    ClothesModel.GLOVES -> ClothesId.GLOVES
    ClothesModel.SUNGLASSES -> ClothesId.SUNGLASSES
    ClothesModel.LONG_SKIRT -> ClothesId.LONG_SKIRT
    ClothesModel.HEAD_SCARF -> ClothesId.HEAD_SCARF
    ClothesModel.BEANIE -> ClothesId.BEANIE
    ClothesModel.CROP_TOP -> ClothesId.CROP_TOP
    ClothesModel.SHIRT -> ClothesId.SHIRT
    ClothesModel.SWEATER -> ClothesId.SWEATER
    ClothesModel.CARDIGAN -> ClothesId.CARDIGAN
    ClothesModel.JUMPER -> ClothesId.JUMPER
    ClothesModel.HOODIE -> ClothesId.HOODIE
    ClothesModel.JEAN_JACKET -> ClothesId.JEAN_JACKET
    ClothesModel.LEATHER_JACKET -> ClothesId.LEATHER_JACKET
    ClothesModel.WINTER_COAT -> ClothesId.WINTER_COAT
    ClothesModel.FLIP_FLOPS -> ClothesId.FLIP_FLOPS
    ClothesModel.SLEVESLESS_LONG_DRESS -> ClothesId.SLEVESLESS_LONG_DRESS
    ClothesModel.SLEEVELESS_SHORT_DRESS -> ClothesId.SLEEVELESS_SHORT_DRESS
    ClothesModel.LONG_SLEEVE_LONG_DRESS -> ClothesId.LONG_SLEEVE_LONG_DRESS
    ClothesModel.LONG_SLEEVE_SHORT_DRESS -> ClothesId.LONG_SLEEVE_SHORT_DRESS
    ClothesModel.FINGERLESS_GLOVES -> ClothesId.FINGERLESS_GLOVES
    ClothesModel.WINTER_GLOVES -> ClothesId.WINTER_GLOVES
    ClothesModel.WINTER_SCARF -> ClothesId.WINTER_SCARF
}
