package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HistoryUiState
import com.youllbecold.trustme.ui.viewmodels.HistoryViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel = koinViewModel()
) {
    HistoryScreen(
        uiState = viewModel.uiState,
    )
}
/**
 * History screen.
 */
@Composable
fun HistoryScreen(
    uiState: Flow<HistoryUiState>,
) {
    val state by uiState.collectAsStateWithLifecycle(HistoryUiState())

    LazyColumn(
        Modifier
            .padding(horizontal = CONTENT_PADDING.dp)
            .fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.padding(BETWEEN_ITEM_PADDING.dp)) }

        items(state.logs.size) { index ->
            LogItem(
                state.logs[index],
                Modifier.padding(BETWEEN_ITEM_PADDING.dp)
            )

            Spacer(modifier = Modifier.padding(BETWEEN_ITEM_PADDING.dp))
        }

        if (state.logs.isEmpty()) {
            item {
                Text(
                    text = "No logs available",
                    modifier = Modifier
                        .fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private const val CONTENT_PADDING = 12
private const val BETWEEN_ITEM_PADDING = 8

@Composable
private fun LogItem(
    log: LogData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(INSIDE_ITEM_PADDING.dp)
        ) {
            Row(modifier = modifier) {
                Text(text = log.toString())
            }
        }
    }
}

private const val INSIDE_ITEM_PADDING = 4

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    YoullBeColdTheme {
        val logEntityItem = LogData(
            1,
            LocalDateTime.now(),
            LocalDateTime.now(),
            WeatherData(0.0, 0.0, 0.0),
            Feeling.WARM,
            emptyList()
        )

        HistoryScreen(
            uiState = flow { emit(HistoryUiState(listOf(logEntityItem, logEntityItem))) },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenEmptyPreview() {
    YoullBeColdTheme {
        HistoryScreen(
            uiState = flow { emit(HistoryUiState(emptyList())) },
        )
    }
}
