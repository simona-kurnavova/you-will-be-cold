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
import com.youllbecold.logdatabase.model.Log
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HistoryViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel = koinViewModel()
) {
    HistoryScreen(
        logs = viewModel.logs,
    )
}
/**
 * History screen.
 */
@Composable
fun HistoryScreen(
    logs: StateFlow<List<Log>>,
) {
    val logList by logs.collectAsStateWithLifecycle()

    LazyColumn(
        Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        items(logList.size) { index ->
            LogItem(
                logList[index],
                Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.padding(8.dp))
        }

        if (logList.isEmpty()) {
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

@Composable
private fun LogItem(
    log: Log,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(modifier = modifier) {
                Text(text = log.toString())
                //Text(text = log.formatedDate())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    YoullBeColdTheme {
        val logEntityItem = Log(
            1,
            LocalDateTime.now(),
            Log.Feeling.WARM,
            weatherData = Log.WeatherData(0.0, 0.0)
        )

        HistoryScreen(
            logs = MutableStateFlow(listOf(logEntityItem, logEntityItem, logEntityItem)),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenEmptyPreview() {
    YoullBeColdTheme {
        HistoryScreen(
            logs = MutableStateFlow(emptyList())
        )
    }
}
