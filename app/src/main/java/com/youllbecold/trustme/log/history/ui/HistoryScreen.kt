package com.youllbecold.trustme.log.history.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.attributes.centered
import com.youllbecold.trustme.common.ui.attributes.defaultMediumErrorTextAttr
import com.youllbecold.trustme.common.ui.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.log.history.ui.components.LogCard
import com.youllbecold.trustme.log.history.ui.model.DeleteStatus
import com.youllbecold.trustme.log.ui.model.LogState
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel = koinViewModel(),
    navigateToEdit: (Int) -> Unit
) {
    val context = LocalContext.current

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(
        logs = state.value.logs.collectAsLazyPagingItems(),
        deleteStatus = state.value.deleteStatus,
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
    deleteStatus: DeleteStatus,
    onAction: (HistoryAction) -> Unit
) {
    val context = LocalContext.current

    when(deleteStatus) {
        DeleteStatus.Success -> {
            Toast.makeText(
                context,
                stringResource(R.string.message_log_deleted),
                Toast.LENGTH_SHORT
            ).show()
        }
        DeleteStatus.Error -> {
            Toast.makeText(
                context,
                stringResource(R.string.message_error_deleting_log),
                Toast.LENGTH_SHORT
            ).show()
        }
        else -> Unit
    }

    HistoryScreenContent(
        logs = logs,
        onAction = onAction,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
    )

}

@Composable
private fun HistoryScreenContent(
    logs: LazyPagingItems<LogState>,
    onAction: (HistoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item { Spacer(modifier = Modifier.height(SPACE_ON_TOP.dp)) }

        items(logs.itemCount) { index ->
            val log = logs[index] ?: return@items

            key(log.id) {
                LogCard(
                    log = log,
                    modifier = Modifier.padding(vertical = BETWEEN_ITEM_PADDING.dp),
                    editAction = { onAction(HistoryAction.Edit(log)) },
                    deleteAction = { onAction(HistoryAction.Delete(log)) },
                )

                Spacer(modifier = Modifier.height(BETWEEN_ITEM_PADDING.dp))
            }
        }

        if (logs.itemCount <= 0 && logs.loadState.isIdle) {
            item {
                ThemedText(
                    text = stringResource(R.string.message_no_logs_available),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(EMPTY_PADDING.dp),
                    textAttr = defaultMediumTextAttr().centered(),
                )
            }
        }

        when {
            logs.loadState.hasError -> item {
                ThemedText(
                    text = stringResource(R.string.generic_error_message),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(EMPTY_PADDING.dp),
                    textAttr = defaultMediumErrorTextAttr()
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

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    YoullBeColdTheme {
        val log = LogState(
            id = null,
            dateTimeState = DateTimeState(
                date = DateState(2022, 1, 1),
                timeFrom = TimeState(12, 0),
                timeTo = TimeState(13, 0)
            ),
        )
        val logs = flow {
            emit(PagingData.from(listOf(log, log, log)))
        }.collectAsLazyPagingItems()

        HistoryScreen(
            logs = logs,
            deleteStatus = DeleteStatus.Idle,
            onAction = {}
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun HistoryScreenEmptyPreview() {
    YoullBeColdTheme {
        HistoryScreen(
            logs = flow { emit(PagingData.empty<LogState>()) }.collectAsLazyPagingItems(),
            deleteStatus = DeleteStatus.Idle,
            onAction = {}
        )
    }
}
