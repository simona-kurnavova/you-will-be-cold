package com.youllbecold.trustme.recommend.ui.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import kotlinx.collections.immutable.PersistentList

/**
 * Data class for recommendation.
 */
@Stable
data class RecommendationState(
    @StringRes
    val uvWarning: Int? = null,
    @StringRes
    val rainWarning: Int? = null,
    val clothes: PersistentList<Clothes>,
    @StringRes
    val certaintyLevel: Int
)
