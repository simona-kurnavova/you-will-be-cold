package com.youllbecold.trustme.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.ui.components.AddLogForm
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.EditLogAction
import com.youllbecold.trustme.ui.viewmodels.EditLogViewModel
import com.youllbecold.trustme.ui.viewmodels.FeelingsState
import com.youllbecold.trustme.ui.viewmodels.LogState
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
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun EditLogScreen(
    state: State<LogState?>,
    onAction: (EditLogAction) -> Unit
) {
    val update: (LogState) -> Unit = { onAction(EditLogAction.SaveProgress(it)) }

    state.value?.let { logState ->
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
            onSave = { onAction(EditLogAction.SaveLog) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditLogScreenPreview() {
    val logState = remember {
        mutableStateOf(
            LogState(
                date = ImmutableDate(LocalDate.now()),
                timeFrom = ImmutableTime(LocalTime.now()),
                timeTo = ImmutableTime(LocalTime.now()),
                feelings = FeelingsState(),
                clothes = persistentSetOf()
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
