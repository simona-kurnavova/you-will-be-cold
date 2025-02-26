package com.youllbecold.trustme.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.ui.components.cards.RecommendationCard
import com.youllbecold.trustme.ui.components.generic.animation.FadingItem
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.RecommendAction
import com.youllbecold.trustme.ui.viewmodels.RecommendUiState
import com.youllbecold.trustme.ui.viewmodels.RecommendViewModel
import com.youllbecold.trustme.ui.viewmodels.state.isIdle
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun RecommendScreenRoot(
    viewModel: RecommendViewModel = koinViewModel()
) {
    RecommendScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(),
        onAction = viewModel::onAction
    )

    // TODO: remove, dummy only
    viewModel.onAction(RecommendAction.UpdateRecommendation(
        date = LocalDate.now(),
        timeFrom = LocalTime.now(),
        timeTo = LocalTime.now().plusHours(2)
    ))
}

@Composable
fun RecommendScreen(
    uiState: State<RecommendUiState>,
    onAction: (RecommendAction) -> Unit
) {
    val state = uiState.value

    Column {
        // TODO: Implement DateTimePicker

        // TODO: Loading

        FadingItem(visible = state.status.isIdle()) {
            state.weatherWithRecommendation?.let {
                RecommendationCard(
                    weatherWithRecommendation = it
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecommendScreenPreview() {
    val state = remember { mutableStateOf(RecommendUiState()) }

    YoullBeColdTheme {
        RecommendScreen(
            uiState = state,
            onAction = {}
        )
    }
}
