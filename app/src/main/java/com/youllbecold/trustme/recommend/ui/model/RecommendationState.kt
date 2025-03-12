package com.youllbecold.trustme.recommend.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import kotlinx.collections.immutable.PersistentList

/**
 * Data class for recommendation.
 */
@Stable
data class RecommendationState(
    val uvWarning: String? = null,
    val rainWarning: String? = null,
    val clothes: PersistentList<Clothes>,
    val certaintyLevel: String
)
