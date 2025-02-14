package com.youllbecold.trustme.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.ui.components.AddLogForm
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.AddLogAction
import com.youllbecold.trustme.ui.viewmodels.AddLogViewModel
import com.youllbecold.trustme.ui.viewmodels.LogState
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddLogRoot(
    navigateToHistory: () -> Unit,
    viewModel: AddLogViewModel = koinViewModel(),
) {
    AddLogScreen(
        state = viewModel.state.collectAsStateWithLifecycle(),
        onAction = { action ->
            when (action) {
                is AddLogAction.SaveLog -> {
                    viewModel.onAction(action)
                    navigateToHistory()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun AddLogScreen(
    state: State<LogState>,
    onAction: (AddLogAction) -> Unit
) {
    val logState = state.value
    val update: (LogState) -> Unit = { onAction(AddLogAction.SaveProgress(it)) }

    AddLogForm(
        date = logState.data,
        timeFrom = logState.timeFrom,
        timeTo = logState.timeTo,
        overallFeeling = logState.overallFeeling,
        clothes = logState.clothes,
        onDateChanged = { update(logState.copy(data = it)) },
        onTimeFromChange = { update(logState.copy(timeFrom = it)) },
        onTimeToChange = { update(logState.copy(timeTo = it)) },
        onOverallFeelingChange = { update(logState.copy(overallFeeling = it)) },
        onClothesCategoryChange = { update(logState.copy(clothes = logState.clothes + it)) },
        removeClothes = { update(logState.copy(clothes = logState.clothes - it)) },
        onSave = { onAction(AddLogAction.SaveLog) },
    )
}

@Preview(showBackground = true)
@Composable
fun AdLogScreenPreview() {
    val logState = remember {
        mutableStateOf(
            LogState(
                data = ImmutableDate(LocalDate.now()),
                timeFrom = ImmutableTime(LocalTime.now()),
                timeTo = ImmutableTime(LocalTime.now()),
                overallFeeling = null,
                clothes = emptySet()
            )
        )
    }

    YoullBeColdTheme {
        AddLogScreen(
            state = logState,
            onAction = {}
        )
    }
}
