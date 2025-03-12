package com.youllbecold.logdatabase.internal.data.mappers

import com.youllbecold.logdatabase.internal.data.model.ClothesId
import com.youllbecold.logdatabase.internal.data.model.FeelingEntity
import com.youllbecold.logdatabase.internal.data.model.LogEntity
import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData

/**
 * Maps [LogEntity] to [LogData].
 */
internal fun LogEntity.toLogData(): LogData = LogData(
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

private fun FeelingEntity.toLogFeeling(): Feeling {
    return when (this) {
        FeelingEntity.NORMAL -> Feeling.NORMAL
        FeelingEntity.COLD -> Feeling.COLD
        FeelingEntity.VERY_COLD -> Feeling.VERY_COLD
        FeelingEntity.WARM -> Feeling.WARM
        FeelingEntity.VERY_WARM -> Feeling.VERY_WARM
    }
}

private fun ClothesId.toClothes(): ClothesModel = when (this) {
    ClothesId.SHORT_SLEEVE -> ClothesModel.SHORT_SLEEVE
    ClothesId.LONG_SLEEVE -> ClothesModel.LONG_SLEEVE
    ClothesId.SHORT_SKIRT -> ClothesModel.SHORT_SKIRT
    ClothesId.SHORTS -> ClothesModel.SHORTS
    ClothesId.JEANS -> ClothesModel.JEANS
    ClothesId.SANDALS -> ClothesModel.SANDALS
    ClothesId.TENNIS_SHOES -> ClothesModel.TENNIS_SHOES
    ClothesId.SHORT_TSHIRT_DRESS -> ClothesModel.SHORT_TSHIRT_DRESS
    ClothesId.BASEBALL_HAT -> ClothesModel.BASEBALL_HAT
    ClothesId.WINTER_HAT -> ClothesModel.WINTER_HAT
    ClothesId.TANK_TOP -> ClothesModel.TANK_TOP
    ClothesId.LIGHT_JACKET -> ClothesModel.LIGHT_JACKET
    ClothesId.WINTER_JACKET -> ClothesModel.WINTER_JACKET
    ClothesId.LEGGINGS -> ClothesModel.LEGGINGS
    ClothesId.WARM_PANTS -> ClothesModel.WARM_PANTS
    ClothesId.WINTER_SHOES -> ClothesModel.WINTER_SHOES
    ClothesId.LONG_TSHIRT_DRESS -> ClothesModel.LONG_TSHIRT_DRESS
    ClothesId.TIGHTS -> ClothesModel.TIGHTS
    ClothesId.SCARF -> ClothesModel.SCARF
    ClothesId.GLOVES -> ClothesModel.GLOVES
    ClothesId.SUNGLASSES -> ClothesModel.SUNGLASSES
    ClothesId.LONG_SKIRT -> ClothesModel.LONG_SKIRT
    ClothesId.SLEEVELESS_SHORT_DRESS -> ClothesModel.SLEEVELESS_SHORT_DRESS
    ClothesId.SLEVESLESS_LONG_DRESS -> ClothesModel.SLEVESLESS_LONG_DRESS
    ClothesId.LONG_SLEEVE_SHORT_DRESS -> ClothesModel.LONG_SLEEVE_SHORT_DRESS
    ClothesId.LONG_SLEEVE_LONG_DRESS -> ClothesModel.LONG_SLEEVE_LONG_DRESS
    ClothesId.HEAD_SCARF -> ClothesModel.HEAD_SCARF
    ClothesId.BEANIE -> ClothesModel.BEANIE
    ClothesId.CROP_TOP -> ClothesModel.CROP_TOP
    ClothesId.SHIRT -> ClothesModel.SHIRT
    ClothesId.SWEATER -> ClothesModel.SWEATER
    ClothesId.CARDIGAN -> ClothesModel.CARDIGAN
    ClothesId.JUMPER -> ClothesModel.JUMPER
    ClothesId.HOODIE -> ClothesModel.HOODIE
    ClothesId.JEAN_JACKET -> ClothesModel.JEAN_JACKET
    ClothesId.LEATHER_JACKET -> ClothesModel.LEATHER_JACKET
    ClothesId.WINTER_COAT -> ClothesModel.WINTER_COAT
    ClothesId.FLIP_FLOPS -> ClothesModel.FLIP_FLOPS
    ClothesId.FINGERLESS_GLOVES -> ClothesModel.FINGERLESS_GLOVES
    ClothesId.WINTER_GLOVES -> ClothesModel.WINTER_GLOVES
    ClothesId.WINTER_SCARF -> ClothesModel.WINTER_SCARF
}
