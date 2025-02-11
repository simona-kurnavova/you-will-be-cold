package com.youllbecold.recomendation.model

import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.LogData

data class Recommendation(
    val clothes: List<Clothes> = emptyList(),
    val certainty: Certainty = Certainty.Low,
    val basedOn: List<LogData> = emptyList()
)

enum class Certainty {
    Low, // Default value was used
    Medium, // Derived from existing data
    High // Matching or almost matching data was found
}