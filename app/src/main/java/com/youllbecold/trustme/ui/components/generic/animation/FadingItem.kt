package com.youllbecold.trustme.ui.components.generic.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FadingItem(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(FADE_DURATION_MS)),
        exit = fadeOut(animationSpec = tween(FADE_DURATION_MS)),
        modifier = modifier
    ) {
        content()
    }
}

private const val FADE_DURATION_MS = 500
