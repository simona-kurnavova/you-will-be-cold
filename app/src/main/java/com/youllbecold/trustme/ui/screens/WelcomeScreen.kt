package com.youllbecold.trustme.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.OverlaySkeleton
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.WelcomeAction
import com.youllbecold.trustme.ui.viewmodels.WelcomeViewModel
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
    // TODO: add how does it works section

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
