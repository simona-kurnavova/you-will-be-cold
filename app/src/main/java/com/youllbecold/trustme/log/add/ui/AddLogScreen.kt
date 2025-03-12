package com.youllbecold.trustme.log.add.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
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
import com.youllbecold.trustme.log.ui.components.LogExitDialog
import com.youllbecold.trustme.log.ui.components.AddLogForm
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.validator.LogStateValidator
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.log.add.ui.model.AddLogUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddLogRoot(
    navigateBack: () -> Unit,
    viewModel: AddLogViewModel = koinViewModel(),
) {
    AddLogScreen(
        state = viewModel.state.collectAsStateWithLifecycle(),
        onAction = { action ->
            when (action) {
                is AddLogAction.ExitForm -> navigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun AddLogScreen(
    state: State<AddLogUiState>,
    onAction: (AddLogAction) -> Unit
) {
    val context = LocalContext.current

    var showExitDialog by remember { mutableStateOf(false) }
    val update: (LogState) -> Unit = { onAction(AddLogAction.SaveProgress(it)) }

    val logState = state.value.logState
    val status = state.value.saveState

    when {
        status.isError() ->
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.toast_error_saving_log),
                Toast.LENGTH_SHORT
            ).show()

        status.isSuccess() -> {
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.toast_saved_log),
                Toast.LENGTH_SHORT
            ).show()

            onAction(AddLogAction.ExitForm)
        }
    }

    AddLogForm(
        dateTimeState = logState.dateTimeState,
        feelings = logState.feelings,
        clothes = logState.clothes,
        onDateTimeChanged = { update(logState.copy(dateTimeState = it)) },
        onFeelingsChange = { update(logState.copy(feelings = it)) },
        onClothesCategoryChange = { update(logState.copy(clothes = it)) },
        onSave = {
            if (!LogStateValidator.validate(context, logState)) {
                return@AddLogForm
            }

            onAction(AddLogAction.SaveLog)
        },
        isSaving = status.isLoading()
    )

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        LogExitDialog(
            onConfirmation = {
                onAction(AddLogAction.ExitForm)
                showExitDialog = false
            },
            onDismiss = { showExitDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdLogScreenPreview() {
    val state = remember {
        mutableStateOf(
            AddLogUiState(
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
        AddLogScreen(
            state = state ,
            onAction = {}
        )
    }
}
