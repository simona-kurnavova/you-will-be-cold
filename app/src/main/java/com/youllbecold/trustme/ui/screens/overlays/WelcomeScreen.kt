package com.youllbecold.trustme.ui.screens.overlays

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.OverlaySkeleton
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

/**
 * Welcome screen for new users.
 */
@Composable
fun WelcomeScreen(
    onPass: () -> Unit
) {
    // TODO: add how does it work section + some image

    OverlaySkeleton(
        title = R.string.welcome_screen_title,
        subtitle = R.string.welcome_screen_description,
        buttonText = R.string.welcome_screen_button_text,
        action = onPass,
    )
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    YoullBeColdTheme {
        WelcomeScreen {}
    }
}
