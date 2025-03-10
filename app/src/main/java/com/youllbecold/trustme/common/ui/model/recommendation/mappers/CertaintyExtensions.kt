package com.youllbecold.trustme.common.ui.model.recommendation.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.trustme.R

@Composable
fun Certainty.getTitle(): String = when (this) {
    Certainty.Low -> stringResource(R.string.certainity_low)
    Certainty.Medium -> stringResource(R.string.certainity_medium)
    Certainty.High -> stringResource(R.string.certainity_high)
}