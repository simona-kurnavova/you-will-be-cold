package com.youllbecold.trustme.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.cards.LogCard
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HistoryAction
import com.youllbecold.trustme.ui.viewmodels.HistoryUiState
import com.youllbecold.trustme.ui.viewmodels.HistoryViewModel
import com.youllbecold.trustme.ui.viewmodels.state.LogState
import kotlinx.collections.immutable.persistentListOf
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
        uiState = viewModel.uiState.collectAsStateWithLifecycle(),
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
    uiState: State<HistoryUiState>,
    onAction: (HistoryAction) -> Unit
) {
    val state = uiState.value

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
    ) {
        item { Spacer(modifier = Modifier.height(BETWEEN_ITEM_PADDING.dp)) }

        items(state.logs.size) { index ->
            key(state.logs[index].id) {
                LogCard(
                    log = state.logs[index],
                    modifier = Modifier.padding(vertical = BETWEEN_ITEM_PADDING.dp),
                    editAction = {
                        onAction(HistoryAction.Edit(state.logs[index]))
                    },
                    deleteAction = {
                        onAction(HistoryAction.Delete(state.logs[index]))
                    },
                )

                Spacer(modifier = Modifier.height(BETWEEN_ITEM_PADDING.dp))
            }
        }

        if (state.logs.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.message_no_logs_available),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(EMPTY_PADDING.dp),
                    textAlign = TextAlign.Center
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

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    YoullBeColdTheme {
        val log = LogState(
            id = null,
            ImmutableDate(LocalDate.now()),
            ImmutableTime(LocalTime.now()),
            ImmutableTime(LocalTime.now()),
        )
        val state = remember {
            mutableStateOf(HistoryUiState(
                persistentListOf(log, log, log)
            ))
        }

        HistoryScreen(
            uiState = state,
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenEmptyPreview() {
    val state = remember {
        mutableStateOf(HistoryUiState(persistentListOf()))
    }

    YoullBeColdTheme {
        HistoryScreen(
            uiState = state,
            onAction = {}
        )
    }
}
