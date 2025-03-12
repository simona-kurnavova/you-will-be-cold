package com.youllbecold.trustme.common.ui.mappers

import androidx.annotation.StringRes
import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import com.youllbecold.trustme.common.ui.model.clothes.ClothesCategory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet

/**
 * Maps the [ClothesModel.Category] to [ClothesCategory].
 */
fun ClothesModel.Category.toClothesCategory(): ClothesCategory =
    ClothesCategory(
        id = this.ordinal,
        name = this.categoryName(),
        icon = this.icon
    )

/**
 * Gets the list of [ClothesModel] for the [ClothesModel.Category].
 */
fun ClothesCategory.getAllItems(): PersistentList<Clothes> =
    ClothesModel.entries.filter {
        it.category.ordinal == this.id
    }.map {
        it.toClothes()
    }.toPersistentList()

/**
 * Gets the set of [ClothesModel] for the [ClothesModel.Category].
 */
fun ClothesCategory.getAllItemsAsSet(): PersistentSet<Clothes> =
    getAllItems().toPersistentSet()

/**
 * Gets the [IconType] for [ClothesModel.Category].
 */
val ClothesModel.Category.icon: IconType
    get() = when (this) {
        ClothesModel.Category.TOPS -> IconType.TShirt
        ClothesModel.Category.HOODIES -> IconType.Sweater
        ClothesModel.Category.JACKETS -> IconType.Jacket
        ClothesModel.Category.BOTTOMS -> IconType.Pants
        ClothesModel.Category.SHOES -> IconType.Shoes
        ClothesModel.Category.HATS -> IconType.Hat
        ClothesModel.Category.ACCESSORIES -> IconType.Sunglasses
        ClothesModel.Category.FULL_BODY -> IconType.Dress
    }

/**
 * Get the title of the [ClothesModel.Category].
 */
@StringRes
private fun ClothesModel.Category.categoryName(): Int = when (this) {
    ClothesModel.Category.TOPS -> R.string.clothes_category_tops
    ClothesModel.Category.HOODIES -> R.string.clothes_category_hoodies
    ClothesModel.Category.JACKETS -> R.string.clothes_category_jackets
    ClothesModel.Category.BOTTOMS -> R.string.clothes_category_pants
    ClothesModel.Category.SHOES -> R.string.clothes_category_shoes
    ClothesModel.Category.HATS -> R.string.clothes_category_hats
    ClothesModel.Category.ACCESSORIES -> R.string.clothes_category_accessories
    ClothesModel.Category.FULL_BODY -> R.string.clothes_category_dresses_and_overalls
}
