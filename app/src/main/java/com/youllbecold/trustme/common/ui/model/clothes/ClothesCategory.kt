package com.youllbecold.trustme.common.ui.model.clothes

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.mappers.toClothesCategory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

/**
 * Represents a category of clothes.
 *
 * @param id The unique identifier of the category.
 * @param name The name of the category.
 * @param icon The icon for the category.
 */
@Stable
data class ClothesCategory(
    val id: Int,
    @StringRes
    val name: Int,
    val icon: IconType
) {
    companion object {
        /**
         * Returns all possible clothes categories
         */
        fun getAll(): PersistentList<ClothesCategory> =
            ClothesModel.Category.entries.map {
                it.toClothesCategory()
            }.toPersistentList()
    }
}