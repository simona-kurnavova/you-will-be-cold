package com.youllbecold.trustme.ui.screens

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
import androidx.compose.runtime.Stable
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
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.cards.RecommendationCard
import com.youllbecold.trustme.ui.components.generic.ThemedCard
import com.youllbecold.trustme.ui.components.generic.ThemedButton
import com.youllbecold.trustme.ui.components.generic.animation.FadingItem
import com.youllbecold.trustme.ui.components.generic.datetime.DateTimeInput
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.RecommendAction
import com.youllbecold.trustme.ui.viewmodels.RecommendUiState
import com.youllbecold.trustme.ui.viewmodels.RecommendViewModel
import com.youllbecold.trustme.ui.viewmodels.state.WeatherWithRecommendation
import com.youllbecold.trustme.ui.viewmodels.state.isError
import com.youllbecold.trustme.ui.viewmodels.state.isIdle
import com.youllbecold.trustme.ui.viewmodels.state.isLoading
import com.youllbecold.trustme.usecases.recommendation.Recommendation
import kotlinx.collections.immutable.persistentListOf
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
}

@Composable
private fun RecommendScreen(
    uiState: State<RecommendUiState>,
    onAction: (RecommendAction) -> Unit
) {
    val state = uiState.value
    val timeNow = LocalTime.now()

    var dateTimeState by remember {
        mutableStateOf(
            DateTimeState(
                date = ImmutableDate(LocalDate.now()),
                timeFrom = ImmutableTime(timeNow),
                timeTo = ImmutableTime(timeNow.plusHours(1)),
            )
        )
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
                    date = dateTimeState.date,
                    timeFrom = dateTimeState.timeFrom,
                    timeTo = dateTimeState.timeTo,
                    onDateChanged = { dateTimeState = dateTimeState.copy(date = it) },
                    onFromTimeSelected = { dateTimeState = dateTimeState.copy(timeFrom = it) },
                    onToTimeSelected = { dateTimeState = dateTimeState.copy(timeTo = it) },
                    allowFuture = true
                )

                Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

                ThemedButton(
                    text = stringResource(R.string.menu_recommendation),
                    onClick = {
                        onAction(
                            RecommendAction.UpdateRecommendation(
                                date = dateTimeState.date.date,
                                timeFrom = dateTimeState.timeFrom.time,
                                timeTo = dateTimeState.timeTo.time
                            )
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
                state.weatherWithRecommendation?.let {
                    RecommendationCard(
                        weatherWithRecommendation = it
                    )
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

@Stable
private data class DateTimeState(
    val date: ImmutableDate,
    val timeFrom: ImmutableTime,
    val timeTo: ImmutableTime
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecommendScreenPreview() {
    val state = remember {
        mutableStateOf(RecommendUiState(
            weatherWithRecommendation = WeatherWithRecommendation(
                weather = persistentListOf(),
                recommendation = Recommendation(
                    UvRecommendation.NoProtection,
                    RainRecommendation.MediumRain,
                    clothes = persistentListOf(Clothes.SHORT_TSHIRT_DRESS),
                    certainty = Certainty.High
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
