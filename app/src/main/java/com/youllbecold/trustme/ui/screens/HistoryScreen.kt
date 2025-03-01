package com.youllbecold.trustme.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.cards.LogCard
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HistoryAction
import com.youllbecold.trustme.ui.viewmodels.HistoryViewModel
import com.youllbecold.trustme.ui.viewmodels.state.LogState
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel = koinViewModel(),
    navigateToEdit: (Int) -> Unit
) {
    val context = LocalContext.current

    HistoryScreen(
        logs = viewModel.uiState.collectAsLazyPagingItems(),
        onAction = { action ->
            when(action) {
                is HistoryAction.Edit -> action.state.id?.let { navigateToEdit(it) }
                    ?: Toast.makeText(
                        context,
                        context.getString(R.string.message_error_editing_log),
                        Toast.LENGTH_SHORT
                    ).show()
                else -> viewModel.onAction(action)
            }
        }
    )
}

/**
 * History screen.
 */
@Composable
private fun HistoryScreen(
    logs: LazyPagingItems<LogState>,
    onAction: (HistoryAction) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
    ) {
        item { Spacer(modifier = Modifier.height(SPACE_ON_TOP.dp)) }

        items(logs.itemCount) { index ->
            val log = logs[index] ?: return@items

            key(log.id) {
                LogCard(
                    log = log,
                    modifier = Modifier.padding(vertical = BETWEEN_ITEM_PADDING.dp),
                    editAction = {
                        onAction(HistoryAction.Edit(log))
                    },
                    deleteAction = {
                        onAction(HistoryAction.Delete(log))
                    },
                )

                Spacer(modifier = Modifier.height(BETWEEN_ITEM_PADDING.dp))
            }
        }

        if (logs.itemCount <= 0 && logs.loadState.isIdle) {
            item {
                Text(
                    text = stringResource(R.string.message_no_logs_available),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(EMPTY_PADDING.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        when {
            logs.loadState.hasError -> item {
                Text(
                    text = stringResource(R.string.generic_error_message),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(EMPTY_PADDING.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            !logs.loadState.isIdle -> item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(EMPTY_PADDING.dp),
                )
            }
        }

        item { Spacer(modifier = Modifier.height(END_SPACE.dp)) }
    }
}

private const val BETWEEN_ITEM_PADDING = 4
private const val END_SPACE = 48
private const val EMPTY_PADDING = 24
private const val HORIZONTAL_SCREEN_PADDING = 16
private const val SPACE_ON_TOP = 16

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    YoullBeColdTheme {
        val log = LogState(
            id = null,
            ImmutableDate(LocalDate.now()),
            ImmutableTime(LocalTime.now()),
            ImmutableTime(LocalTime.now()),
        )
        val state = flow {
            emit(PagingData.from(listOf(log, log, log)))
        }

        HistoryScreen(
            logs = state.collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenEmptyPreview() {
    YoullBeColdTheme {
        HistoryScreen(
            logs = flow {
                emit(PagingData.empty<LogState>())
            }.collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}
