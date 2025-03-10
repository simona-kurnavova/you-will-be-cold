package com.youllbecold.recomendation.model

/**
 * Certainty level of the recommendation.
 */
enum class Certainty {
    /**
     * Default value was used, we are just guessing here.
     */
    Low,

    /**
     * Derived from existing data with some confidence.
     */
    Medium,

    /**
     * Derived from existing data with reasonably high confidence.
     */
    High
}
