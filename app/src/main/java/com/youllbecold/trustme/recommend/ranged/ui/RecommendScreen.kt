package com.youllbecold.trustme.recommend.ranged.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.animation.FadingItem
import com.youllbecold.trustme.recommend.ranged.ui.components.RecommendationCard
import com.youllbecold.trustme.common.ui.components.themed.ThemedButton
import com.youllbecold.trustme.common.ui.components.themed.ThemedCard
import com.youllbecold.trustme.common.ui.components.datetime.DateTimeInput
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.recommend.usecases.model.mappers.feelLikeDescription
import com.youllbecold.trustme.recommend.usecases.model.mappers.temperatureRangeDescription
import com.youllbecold.trustme.recommend.usecases.model.RecommendationState
import com.youllbecold.trustme.recommend.usecases.model.WeatherWithRecommendation
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.recommend.ranged.ui.model.RecommendUiState
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun RecommendScreenRoot(
    viewModel: RecommendViewModel = koinViewModel()
) {
    RecommendScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(),
        onAction = viewModel::onAction
    )
}

@Composable
private fun RecommendScreen(
    uiState: State<RecommendUiState>,
    onAction: (RecommendAction) -> Unit
) {
    val state = uiState.value
    val dateTimeNow = LocalDateTime.now()

    var dateTimeState by remember {
        // Initial state
        mutableStateOf(DateTimeState.fromDateTime(dateTimeNow, dateTimeNow.plusHours(1)))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = rememberScrollState())
            .padding(vertical = VERTICAL_PADDING.dp, horizontal = HORIZONTAL_SCREEN_PADDING.dp)
    ) {
        ThemedCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                DateTimeInput(
                    dateTimeState = dateTimeState,
                    onDatetimeChanged = { dateTimeState = it },
                    allowFuture = true
                )

                Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

                ThemedButton(
                    text = stringResource(R.string.menu_recommendation),
                    onClick = {
                        onAction(
                            RecommendAction.UpdateRecommendation(dateTimeState)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

        when {
            state.status.isLoading() -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = PROGRESS_INDICATOR_PADDING.dp)
            )
            state.status.isError() -> Text(
                    text = stringResource(R.string.recom_no_recommendation),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(CARD_INTERNAL_PADDING.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )

            else -> FadingItem(visible = state.status.isIdle()) {
                state.weatherWithRecommendation?.let { weatherWithRec ->
                    weatherWithRec.recommendationState?.let {
                        RecommendationCard(
                            temperatureRangeDescription = weatherWithRec.temperatureRangeDescription(),
                            feelsLikeDescription = weatherWithRec.feelLikeDescription(),
                            uvWarning = it.uvWarning,
                            rainWarning = it.rainWarning,
                            clothes = it.clothes,
                            certaintyLevelDescription = it.certaintyLevel
                        )
                    }
                }
            }
        }
    }
}

private const val PROGRESS_INDICATOR_PADDING = 24
private const val CARD_INTERNAL_PADDING = 12
private const val SPACE_BETWEEN_ITEMS = 12
private const val VERTICAL_PADDING = 16
private const val HORIZONTAL_SCREEN_PADDING = 12

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecommendScreenPreview() {
    val state = remember {
        mutableStateOf(RecommendUiState(
            weatherWithRecommendation = WeatherWithRecommendation(
                weather = persistentListOf(),
                recommendationState = RecommendationState(
                    "UV warning",
                    "Rain warning",
                    clothes = persistentListOf(Clothes.SHORT_TSHIRT_DRESS),
                    certaintyLevel = "High"
                )
            )
        ))
    }

    YoullBeColdTheme {
        RecommendScreen(
            uiState = state,
            onAction = {}
        )
    }
}
