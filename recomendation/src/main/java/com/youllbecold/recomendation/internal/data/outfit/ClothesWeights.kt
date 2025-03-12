package com.youllbecold.recomendation.internal.data.outfit

import com.youllbecold.logdatabase.model.ClothesModel

/**
 * Weight of clothes for different body parts.
 */
internal class ClothesWeight(
    val head: Int = 0,
    val neck: Int = 0,
    val top: Int = 0,
    val bottom: Int = 0,
    val hands: Int = 0,
    val feet: Int = 0,
)

/**
 * Weights of clothes for different clothes.
 */
internal val clothesModelWeights: Map<ClothesModel, ClothesWeight> = ClothesModel.entries.associateWith {
    when(it) {
        ClothesModel.HEAD_SCARF -> ClothesWeight(head = 1)
        ClothesModel.BASEBALL_HAT -> ClothesWeight(head = 2)
        ClothesModel.BEANIE -> ClothesWeight(head = 3)
        ClothesModel.WINTER_HAT -> ClothesWeight(head = 4)

        ClothesModel.TANK_TOP -> ClothesWeight(top = 1)
        ClothesModel.CROP_TOP -> ClothesWeight(top = 1)
        ClothesModel.SHORT_SLEEVE -> ClothesWeight(top = 2)
        ClothesModel.LONG_SLEEVE -> ClothesWeight(top = 3)
        ClothesModel.SHIRT -> ClothesWeight(top = 3)

        ClothesModel.SWEATER -> ClothesWeight(top = 4)
        ClothesModel.CARDIGAN -> ClothesWeight(top = 4)
        ClothesModel.JUMPER -> ClothesWeight(top = 4)
        ClothesModel.HOODIE -> ClothesWeight(top = 4)

        ClothesModel.LIGHT_JACKET -> ClothesWeight(top = 5)
        ClothesModel.JEAN_JACKET -> ClothesWeight(top = 5)
        ClothesModel.LEATHER_JACKET -> ClothesWeight(top = 6)
        ClothesModel.WINTER_COAT -> ClothesWeight(top = 7)
        ClothesModel.WINTER_JACKET -> ClothesWeight(top = 8)

        ClothesModel.SHORTS -> ClothesWeight(bottom = 1)
        ClothesModel.SHORT_SKIRT -> ClothesWeight(bottom = 1)
        ClothesModel.LEGGINGS -> ClothesWeight(bottom = 2)
        ClothesModel.LONG_SKIRT -> ClothesWeight(bottom = 2)
        ClothesModel.JEANS -> ClothesWeight(bottom = 3)
        ClothesModel.WARM_PANTS -> ClothesWeight(bottom = 3)

        ClothesModel.FLIP_FLOPS -> ClothesWeight(feet = 1)
        ClothesModel.SANDALS -> ClothesWeight(feet = 2)
        ClothesModel.TENNIS_SHOES -> ClothesWeight(feet = 3)
        ClothesModel.WINTER_SHOES -> ClothesWeight(feet = 3)

        ClothesModel.FINGERLESS_GLOVES -> ClothesWeight(hands = 1)
        ClothesModel.GLOVES -> ClothesWeight(hands = 2)
        ClothesModel.WINTER_GLOVES -> ClothesWeight(hands = 3)

        ClothesModel.SCARF -> ClothesWeight(neck = 1)
        ClothesModel.WINTER_SCARF -> ClothesWeight(neck = 2)

        ClothesModel.TIGHTS -> ClothesWeight(bottom = 1)

        // Accessories and full body are never recommended, return empty
        else -> ClothesWeight()
    }
}
