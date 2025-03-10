package com.youllbecold.trustme.overlays.welcome.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.overlays.ui.OverlaySkeleton
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreenRoot(
    viewModel: WelcomeViewModel = koinViewModel(),
    onNavigateToDashboard: () -> Unit,
    onNavigateToLocation: () -> Unit
) {
    WelcomeScreen { action ->
        when(action) {
            WelcomeAction.PassWelcomeScreen -> {
                viewModel.onAction(action)
                if (viewModel.hasLocationPermission) {
                    onNavigateToDashboard()
                } else {
                    onNavigateToLocation()
                }
            }
        }
    }
}
/**
 * Welcome screen for new users.
 */
@Composable
private fun WelcomeScreen(
    onAction: (WelcomeAction) -> Unit
) {
    OverlaySkeleton(
        title = R.string.welcome_screen_title,
        subtitle = R.string.welcome_screen_description,
        buttonText = R.string.welcome_screen_button_text,
        action = { onAction(WelcomeAction.PassWelcomeScreen) },
        image = R.drawable.thermometer_0
    )
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    YoullBeColdTheme {
        WelcomeScreen {}
    }
}
