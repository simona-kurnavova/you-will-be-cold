package com.youllbecold.trustme.ui.components.generic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
fun FadingItem(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(FADE_DURATION_MS)),
        exit = fadeOut(animationSpec = tween(FADE_DURATION_MS))
    ) {
        content()
    }
}

private const val FADE_DURATION_MS = 800
