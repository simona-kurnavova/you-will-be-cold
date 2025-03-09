package com.youllbecold.logdatabase.internal.data.mappers

import com.youllbecold.logdatabase.internal.data.model.ClothesId
import com.youllbecold.logdatabase.internal.data.model.FeelingEntity
import com.youllbecold.logdatabase.internal.data.model.LogEntity
import com.youllbecold.logdatabase.model.Clothes
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
