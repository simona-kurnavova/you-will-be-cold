package com.youllbecold.recomendation.internal.model

import com.youllbecold.logdatabase.model.Clothes

internal class ClothesWeight(
    val head: Int = 0,
    val neck: Int = 0,
    val top: Int = 0,
    val bottom: Int = 0,
    val hands: Int = 0,
    val feet: Int = 0,
)

internal val clothesWeights: Map<Clothes, ClothesWeight> = Clothes.entries.associateWith {
    when(it) {
        Clothes.HEAD_SCARF -> ClothesWeight(head = 1)
        Clothes.BASEBALL_HAT -> ClothesWeight(head = 2)
        Clothes.BEANIE -> ClothesWeight(head = 3)
        Clothes.WINTER_HAT -> ClothesWeight(head = 4)

        Clothes.TANK_TOP -> ClothesWeight(top = 1)
        Clothes.CROP_TOP -> ClothesWeight(top = 1)
        Clothes.SHORT_SLEEVE -> ClothesWeight(top = 2)
        Clothes.LONG_SLEEVE -> ClothesWeight(top = 3)
        Clothes.SHIRT -> ClothesWeight(top = 3)

        Clothes.SWEATER -> ClothesWeight(top = 4)
        Clothes.CARDIGAN -> ClothesWeight(top = 4)
        Clothes.JUMPER -> ClothesWeight(top = 4)
        Clothes.HOODIE -> ClothesWeight(top = 4)

        Clothes.LIGHT_JACKET -> ClothesWeight(top = 5)
        Clothes.JEAN_JACKET -> ClothesWeight(top = 5)
        Clothes.LEATHER_JACKET -> ClothesWeight(top = 6)
        Clothes.WINTER_COAT -> ClothesWeight(top = 7)
        Clothes.WINTER_JACKET -> ClothesWeight(top = 8)

        Clothes.SHORTS -> ClothesWeight(bottom = 1)
        Clothes.SHORT_SKIRT -> ClothesWeight(bottom = 1)
        Clothes.LEGGINGS -> ClothesWeight(bottom = 2)
        Clothes.LONG_SKIRT -> ClothesWeight(bottom = 2)
        Clothes.JEANS -> ClothesWeight(bottom = 3)
        Clothes.WARM_PANTS -> ClothesWeight(bottom = 3)

        Clothes.FLIP_FLOPS -> ClothesWeight(feet = 1)
        Clothes.SANDALS -> ClothesWeight(feet = 2)
        Clothes.TENNIS_SHOES -> ClothesWeight(feet = 3)
        Clothes.WINTER_SHOES -> ClothesWeight(feet = 3)

        Clothes.FINGERLESS_GLOVES -> ClothesWeight(hands = 1)
        Clothes.GLOVES -> ClothesWeight(hands = 2)
        Clothes.WINTER_GLOVES -> ClothesWeight(hands = 3)

        Clothes.SCARF -> ClothesWeight(neck = 1)
        Clothes.WINTER_SCARF -> ClothesWeight(neck = 2)

        Clothes.TIGHTS -> ClothesWeight(bottom = 1)

        // Accessories and full body are never recommended, return empty
        else -> ClothesWeight()
    }
}

internal val categorizedClothesWeights = clothesWeights.entries.groupBy { it.key.category }
