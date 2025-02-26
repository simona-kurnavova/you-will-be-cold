package com.youllbecold.trustme.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.AddLogForm
import com.youllbecold.trustme.ui.components.LogExitDialog
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.EditLogAction
import com.youllbecold.trustme.ui.viewmodels.EditLogUiState
import com.youllbecold.trustme.ui.viewmodels.EditLogViewModel
import com.youllbecold.trustme.ui.viewmodels.EditingState
import com.youllbecold.trustme.ui.viewmodels.state.FeelingsState
import com.youllbecold.trustme.ui.viewmodels.state.LogState
import com.youllbecold.trustme.ui.viewmodels.state.validate
import kotlinx.collections.immutable.persistentSetOf
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun EditLogRoot(
    id: Int,
    navigateBack: () -> Unit,
    viewModel: EditLogViewModel = koinViewModel(),
) {
    LaunchedEffect(id) {
        viewModel.onAction(EditLogAction.StartEdit(id))
    }

    EditLogScreen(
        state = viewModel.state.collectAsStateWithLifecycle(),
        onAction = { action ->
            when (action) {
                is EditLogAction.SaveLog -> {
                    viewModel.onAction(action)
                    navigateBack()
                }
                is EditLogAction.ExitForm -> navigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun EditLogScreen(
    state: State<EditLogUiState>,
    onAction: (EditLogAction) -> Unit
) {
    val update: (LogState) -> Unit = { onAction(EditLogAction.SaveProgress(it)) }
    val context = LocalContext.current

    var showExitDialog by remember { mutableStateOf(false) }

    if (state.value.editState == EditingState.Error) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.toast_error_editing_log),
            Toast.LENGTH_SHORT
        ).show()
    }

    state.value.logState?.let { logState ->
        AddLogForm(
            date = logState.date,
            timeFrom = logState.timeFrom,
            timeTo = logState.timeTo,
            feelings = logState.feelings,
            clothes = logState.clothes,
            onDateChanged = { update(logState.copy(date = it)) },
            onTimeFromChange = { update(logState.copy(timeFrom = it)) },
            onTimeToChange = { update(logState.copy(timeTo = it)) },
            onFeelingsChange = { update(logState.copy(feelings = it)) },
            onClothesCategoryChange = { update(logState.copy(clothes = it)) },
            onSave = {
                if (!logState.validate(context)) {
                    return@AddLogForm
                }

                onAction(EditLogAction.SaveLog)

                // TODO: load until result is received, this is a temporary solution, hehe.

                if (state.value.editState != EditingState.Error) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_saved_log),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
        )
    }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        LogExitDialog(
            onConfirmation = {
                onAction(EditLogAction.ExitForm)
                showExitDialog = false
            },
            onDismiss = { showExitDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditLogScreenPreview() {
    val logState = remember {
        mutableStateOf(
            EditLogUiState(
                logState =
                    LogState(
                        date = ImmutableDate(LocalDate.now()),
                        timeFrom = ImmutableTime(LocalTime.now()),
                        timeTo = ImmutableTime(LocalTime.now()),
                        feelings = FeelingsState(),
                        clothes = persistentSetOf()
                    )
            )
        )
    }

    YoullBeColdTheme {
        EditLogScreen(
            state = logState,
            onAction = {}
        )
    }
}
