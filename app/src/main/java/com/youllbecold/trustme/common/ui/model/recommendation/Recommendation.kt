package com.youllbecold.trustme.common.ui.model.recommendation

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import kotlinx.collections.immutable.PersistentList

// TODO: mapping for UvRecommendation, RainRecommendation, Clothes, Certainty

/**
 * Data class for recommendation.
 */
@Stable
data class Recommendation(
    val uvLevel: UvRecommendation,
    val rainLevel: RainRecommendation,
    val clothes: PersistentList<Clothes>,
    val certainty: Certainty
)