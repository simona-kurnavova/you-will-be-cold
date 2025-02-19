package com.youllbecold.trustme.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.AddLogForm
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.AddLogAction
import com.youllbecold.trustme.ui.viewmodels.AddLogUiState
import com.youllbecold.trustme.ui.viewmodels.AddLogViewModel
import com.youllbecold.trustme.ui.viewmodels.FeelingsState
import com.youllbecold.trustme.ui.viewmodels.LogState
import com.youllbecold.trustme.ui.viewmodels.SavingState
import com.youllbecold.trustme.ui.viewmodels.validate
import kotlinx.collections.immutable.persistentSetOf
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddLogRoot(
    navigateBack: () -> Unit,
    viewModel: AddLogViewModel = koinViewModel(),
) {
    AddLogScreen(
        state = viewModel.state.collectAsStateWithLifecycle(),
        onAction = { action ->
            when (action) {
                is AddLogAction.SaveLog -> {
                    viewModel.onAction(action)
                    navigateBack()
                }
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
    val logState = state.value.logState
    val context = LocalContext.current
    val update: (LogState) -> Unit = { onAction(AddLogAction.SaveProgress(it)) }

    if (state.value.saveState == SavingState.Error) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.toast_error_saving_log),
            Toast.LENGTH_SHORT
        ).show()
    }

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

            onAction(AddLogAction.SaveLog)

            // TODO: load until result is received, this is a temporary solution, hehe.

            if (state.value.saveState != SavingState.Error) {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_saved_log),
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun AdLogScreenPreview() {
    val state = remember {
        mutableStateOf(
            AddLogUiState(
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
        AddLogScreen(
            state = state ,
            onAction = {}
        )
    }
}
