package com.youllbecold.trustme.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.inputs.SelectableItemContent

/**
 * Gets the [IconType] for [Clothes.Category].
 */
val Clothes.Category.icon: IconType
   get() = when (this) {
        Clothes.Category.TOPS -> IconType.TShirt
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
    get() = this.category.icon

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
    Clothes.SHORT_DRESS -> stringResource(R.string.clothes_dress)
    Clothes.LONG_DRESS -> stringResource(R.string.clothes_dress_long)
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
}

/**
 * Converts a set of [Clothes] to a list of [SelectableItemContent].
 */
@Composable
fun List<Clothes>.toSelectableItemContent(): List<SelectableItemContent> =
    this.map { item ->
        SelectableItemContent(
            title = item.getTitle(),
            iconType = item.icon,
        )
    }

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