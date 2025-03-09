package com.youllbecold.logdatabase.internal.data.mappers

import com.youllbecold.logdatabase.internal.data.model.ClothesId
import com.youllbecold.logdatabase.internal.data.model.FeelingEntity
import com.youllbecold.logdatabase.internal.data.model.LogEntity
import com.youllbecold.logdatabase.model.Clothes
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
