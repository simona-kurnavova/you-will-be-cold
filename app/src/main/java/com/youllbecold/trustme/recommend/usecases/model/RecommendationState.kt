package com.youllbecold.trustme.recommend.usecases.model

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import kotlinx.collections.immutable.PersistentList

// TODO: mapping for Clothes

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
