package com.youllbecold.trustme.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.inputs.SelectableItemContent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet

/**
 * Gets the [IconType] for [Clothes.Category].
 */
val Clothes.Category.icon: IconType
    get() = when (this) {
        Clothes.Category.TOPS -> IconType.TShirt
        Clothes.Category.HOODIES -> IconType.Sweater
        Clothes.Category.JACKETS -> IconType.Jacket
        Clothes.Category.BOTTOMS -> IconType.Pants
        Clothes.Category.SHOES -> IconType.Shoes
        Clothes.Category.HATS -> IconType.Hat
        Clothes.Category.ACCESSORIES -> IconType.Sunglasses
        Clothes.Category.FULL_BODY -> IconType.Dress
    }

/**
 * Get the title of the [Clothes.Category].
 */
@Composable
fun Clothes.Category.getTitle(): String = when (this) {
        Clothes.Category.TOPS -> stringResource(R.string.clothes_category_tops)
        Clothes.Category.HOODIES -> stringResource(R.string.clothes_category_hoodies)
        Clothes.Category.JACKETS -> stringResource(R.string.clothes_category_jackets)
        Clothes.Category.BOTTOMS -> stringResource(R.string.clothes_category_pants)
        Clothes.Category.SHOES -> stringResource(R.string.clothes_category_shoes)
        Clothes.Category.HATS -> stringResource(R.string.clothes_category_hats)
        Clothes.Category.ACCESSORIES -> stringResource(R.string.clothes_category_accessories)
        Clothes.Category.FULL_BODY -> stringResource(R.string.clothes_category_dresses_and_overalls)
    }

/**
 * Gets the [IconType] for [Clothes].
 */
val Clothes.icon: IconType
    get() = when(this) {
        Clothes.TANK_TOP -> IconType.TankTop
        Clothes.LONG_SLEEVE,
        Clothes.SHIRT -> IconType.Shirt
        Clothes.SHORTS -> IconType.Shorts
        Clothes.SHORT_SKIRT,
        Clothes.LONG_SKIRT -> IconType.Skirt
        else -> this.category.icon
    }

/**
 * Get the title of the [Clothes].
 */
@Composable
fun Clothes.getTitle(): String = when (this) {
    Clothes.SHORT_SLEEVE -> stringResource(R.string.clothes_short_sleeve)
    Clothes.LONG_SLEEVE -> stringResource(R.string.clothes_long_sleeve)
    Clothes.SHORT_SKIRT -> stringResource(R.string.clothes_short_skirt)
    Clothes.SHORTS -> stringResource(R.string.clothes_shorts)
    Clothes.JEANS -> stringResource(R.string.clothes_jeans)
    Clothes.SANDALS -> stringResource(R.string.clothes_sandals)
    Clothes.TENNIS_SHOES -> stringResource(R.string.clothes_tennis_shoes)
    Clothes.SHORT_TSHIRT_DRESS -> stringResource(R.string.clothes_dress)
    Clothes.LONG_TSHIRT_DRESS -> stringResource(R.string.clothes_dress_long)
    Clothes.BASEBALL_HAT -> stringResource(R.string.baseball_hat)
    Clothes.WINTER_HAT -> stringResource(R.string.winter_hat)
    Clothes.TANK_TOP -> stringResource(R.string.tank_top)
    Clothes.LIGHT_JACKET -> stringResource(R.string.light_jacket)
    Clothes.WINTER_JACKET -> stringResource(R.string.winter_jacker)
    Clothes.LEGGINGS -> stringResource(R.string.legging)
    Clothes.WARM_PANTS -> stringResource(R.string.warm_pants)
    Clothes.WINTER_SHOES -> stringResource(R.string.winter_shoes)
    Clothes.TIGHTS -> stringResource(R.string.tights)
    Clothes.SCARF -> stringResource(R.string.scarf)
    Clothes.GLOVES -> stringResource(R.string.gloves)
    Clothes.SUNGLASSES -> stringResource(R.string.sunglasses)
    Clothes.LONG_SKIRT -> stringResource(R.string.clothes_long_skirt)
    Clothes.HEAD_SCARF -> stringResource(R.string.clothes_head_scarf)
    Clothes.BEANIE -> stringResource(R.string.clothes_beanie)
    Clothes.CROP_TOP -> stringResource(R.string.clothes_crop_top)
    Clothes.SHIRT -> stringResource(R.string.clothes_shirt)
    Clothes.SWEATER -> stringResource(R.string.clothes_sweater)
    Clothes.CARDIGAN -> stringResource(R.string.clothes_cardigan)
    Clothes.JUMPER -> stringResource(R.string.clothes_jumper)
    Clothes.HOODIE -> stringResource(R.string.clothes_hoodie)
    Clothes.JEAN_JACKET -> stringResource(R.string.clothes_jean_jacket)
    Clothes.LEATHER_JACKET -> stringResource(R.string.clothes_leather_jacket)
    Clothes.WINTER_COAT -> stringResource(R.string.clothes_winter_coat)
    Clothes.FLIP_FLOPS -> stringResource(R.string.clothes_flip_flops)
    Clothes.SLEVESLESS_LONG_DRESS -> stringResource(R.string.clothes_sleeveless_long_dress)
    Clothes.SLEEVELESS_SHORT_DRESS -> stringResource(R.string.clothes_sleeveless_short_dress)
    Clothes.LONG_SLEEVE_LONG_DRESS -> stringResource(R.string.clothes_long_sleeve_long_dress)
    Clothes.LONG_SLEEVE_SHORT_DRESS -> stringResource(R.string.clothes_long_sleeve_short_dress)
    Clothes.FINGERLESS_GLOVES -> stringResource(R.string.clothes_fingerless_gloves)
    Clothes.WINTER_GLOVES -> stringResource(R.string.clothes_winter_gloves)
    Clothes.WINTER_SCARF -> stringResource(R.string.clothes_winter_scarf)
}

/**
 * Converts a set of [Clothes] to a list of [SelectableItemContent].
 */
@Composable
fun List<Clothes>.toSelectableItemContent(): PersistentList<SelectableItemContent> =
    this.map { item ->
        SelectableItemContent(
            title = item.getTitle(),
            iconType = item.icon,
        )
    }.toPersistentList()

/**
 * Gets the list of [Clothes] for the [Clothes.Category].
 */
val Clothes.Category.items: List<Clothes>
    get() = Clothes.entries.filter { it.category == this }

/**
 * Filters the list of [Clothes] by the [Clothes.Category].
 */
fun Set<Clothes>.withCategory(category: Clothes.Category): List<Clothes> =
    this.filter { it.category == category }

@Composable
fun Certainty.getTitle(): String = when (this) {
    Certainty.Low -> stringResource(R.string.certainity_low)
    Certainty.Medium -> stringResource(R.string.certainity_medium)
    Certainty.High -> stringResource(R.string.certainity_high)
}