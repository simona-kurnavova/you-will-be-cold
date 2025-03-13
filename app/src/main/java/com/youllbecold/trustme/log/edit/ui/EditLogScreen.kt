package com.youllbecold.trustme.log.edit.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.log.edit.ui.model.EditLogUiState
import com.youllbecold.trustme.log.ui.components.AddLogForm
import com.youllbecold.trustme.log.ui.model.LogState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import org.koin.androidx.compose.koinViewModel

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
    val context = LocalContext.current
    val update: (LogState) -> Unit = { onAction(EditLogAction.SaveProgress(it)) }

    val status = state.value.editState
    when {
        status.isError() ->
            Toast.makeText(
            context,
            stringResource(R.string.toast_error_editing_log),
            Toast.LENGTH_SHORT
        ).show()

        status.isSuccess() -> {
            Toast.makeText(
                context,
                stringResource(R.string.toast_saved_log),
                Toast.LENGTH_SHORT
            ).show()

            onAction(EditLogAction.ExitForm)
        }
    }

    state.value.logState?.let { logState ->
        AddLogForm(
            dateTimeState = logState.dateTimeState,
            feelings = logState.feelings,
            clothes = logState.clothes,
            onDateTimeChanged = { update(logState.copy(dateTimeState = it)) },
            onFeelingsChange = { update(logState.copy(feelings = it)) },
            onClothesCategoryChange = { update(logState.copy(clothes = it)) },
            onSave = { onAction(EditLogAction.SaveLog) },
            onExit = { onAction(EditLogAction.ExitForm) },
            isSaving = status.isLoading()
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
                        dateTimeState = DateTimeState(
                            date = DateState(2022, 1, 1),
                            timeFrom = TimeState(12, 0),
                            timeTo = TimeState(13, 0)
                        ),
                        feelings = persistentListOf(),
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
