package com.youllbecold.trustme.common.ui.mappers

import androidx.annotation.StringRes
import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import com.youllbecold.trustme.common.ui.model.clothes.ClothesCategory

/**
 * Maps the [ClothesModel] to [Clothes].
 */
fun ClothesModel.toClothes(): Clothes = Clothes(
    id = this.ordinal,
    name = clothesName(),
    category = category.toClothesCategory(),
    icon = icon,
)

/**
 * Maps the [Clothes] to [ClothesModel].
 */
fun Clothes.toClothesModel(): ClothesModel? =
    ClothesModel.entries.find { it.ordinal == id }

/**
 * Filters the list of [Clothes] by the [ClothesCategory].
 */
fun Set<Clothes>.withCategory(category: ClothesCategory): List<Clothes> =
    this.filter { it.category == category }

/**
 * Gets the [IconType] for [ClothesModel].
 */
private val ClothesModel.icon: IconType
    get() = when(this) {
        ClothesModel.TANK_TOP -> IconType.TankTop
        ClothesModel.LONG_SLEEVE,
        ClothesModel.SHIRT -> IconType.Shirt
        ClothesModel.SHORTS -> IconType.Shorts
        ClothesModel.SHORT_SKIRT,
        ClothesModel.LONG_SKIRT -> IconType.Skirt
        else -> this.category.icon
    }

/**
 * Get the title of the [ClothesModel].
 */
@StringRes
private fun ClothesModel.clothesName(): Int = when (this) {
    ClothesModel.SHORT_SLEEVE -> R.string.clothes_short_sleeve
    ClothesModel.LONG_SLEEVE -> R.string.clothes_long_sleeve
    ClothesModel.SHORT_SKIRT -> R.string.clothes_short_skirt
    ClothesModel.SHORTS -> R.string.clothes_shorts
    ClothesModel.JEANS -> R.string.clothes_jeans
    ClothesModel.SANDALS -> R.string.clothes_sandals
    ClothesModel.TENNIS_SHOES -> R.string.clothes_tennis_shoes
    ClothesModel.SHORT_TSHIRT_DRESS -> R.string.clothes_dress
    ClothesModel.LONG_TSHIRT_DRESS -> R.string.clothes_dress_long
    ClothesModel.BASEBALL_HAT -> R.string.baseball_hat
    ClothesModel.WINTER_HAT -> R.string.winter_hat
    ClothesModel.TANK_TOP -> R.string.tank_top
    ClothesModel.LIGHT_JACKET -> R.string.light_jacket
    ClothesModel.WINTER_JACKET -> R.string.winter_jacker
    ClothesModel.LEGGINGS -> R.string.legging
    ClothesModel.WARM_PANTS -> R.string.warm_pants
    ClothesModel.WINTER_SHOES -> R.string.winter_shoes
    ClothesModel.TIGHTS -> R.string.tights
    ClothesModel.SCARF -> R.string.scarf
    ClothesModel.GLOVES -> R.string.gloves
    ClothesModel.SUNGLASSES -> R.string.sunglasses
    ClothesModel.LONG_SKIRT -> R.string.clothes_long_skirt
    ClothesModel.HEAD_SCARF -> R.string.clothes_head_scarf
    ClothesModel.BEANIE -> R.string.clothes_beanie
    ClothesModel.CROP_TOP -> R.string.clothes_crop_top
    ClothesModel.SHIRT -> R.string.clothes_shirt
    ClothesModel.SWEATER -> R.string.clothes_sweater
    ClothesModel.CARDIGAN -> R.string.clothes_cardigan
    ClothesModel.JUMPER -> R.string.clothes_jumper
    ClothesModel.HOODIE -> R.string.clothes_hoodie
    ClothesModel.JEAN_JACKET -> R.string.clothes_jean_jacket
    ClothesModel.LEATHER_JACKET -> R.string.clothes_leather_jacket
    ClothesModel.WINTER_COAT -> R.string.clothes_winter_coat
    ClothesModel.FLIP_FLOPS -> R.string.clothes_flip_flops
    ClothesModel.SLEVESLESS_LONG_DRESS -> R.string.clothes_sleeveless_long_dress
    ClothesModel.SLEEVELESS_SHORT_DRESS -> R.string.clothes_sleeveless_short_dress
    ClothesModel.LONG_SLEEVE_LONG_DRESS -> R.string.clothes_long_sleeve_long_dress
    ClothesModel.LONG_SLEEVE_SHORT_DRESS -> R.string.clothes_long_sleeve_short_dress
    ClothesModel.FINGERLESS_GLOVES -> R.string.clothes_fingerless_gloves
    ClothesModel.WINTER_GLOVES -> R.string.clothes_winter_gloves
    ClothesModel.WINTER_SCARF -> R.string.clothes_winter_scarf
}
