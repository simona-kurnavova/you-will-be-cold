package com.youllbecold.trustme.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
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
        onAction = { action ->
            when (action) {
                is AddLogAction.SaveLog -> {
                    viewModel.onAction(action)
                    navigateToHistory()
                }
            }
        }
    )
}

@Composable
fun AddLogScreen(
    onAction: (AddLogAction) -> Unit
) {
    val currentTime = LocalTime.now()

    var logState by remember {
        mutableStateOf(
            LogState(
                data = ImmutableDate(LocalDate.now()),
                timeFrom = ImmutableTime(currentTime.minusHours(1)),
                timeTo = ImmutableTime(currentTime),
                overallFeeling = null,
                clothes = emptySet()
            )
        )
    }

    AddLogForm(
        date = logState.data,
        timeFrom = logState.timeFrom,
        timeTo = logState.timeTo,
        overallFeeling = logState.overallFeeling,
        clothes = logState.clothes,
        onDateChanged = { logState = logState.copy(data = it) },
        onTimeFromChange = { logState = logState.copy(timeFrom = it) },
        onTimeToChange = { logState = logState.copy(timeTo = it) },
        onOverallFeelingChange = { logState = logState.copy(overallFeeling = it) },
        onClothesCategoryChange = { logState = logState.copy(clothes = logState.clothes + it) },
        removeClothes = { logState = logState.copy(clothes = logState.clothes - it) },
        onSave = { onAction(AddLogAction.SaveLog(logState)) },
    )
}

@Preview(showBackground = true)
@Composable
fun AdLogScreenPreview() {
    YoullBeColdTheme {
        AddLogScreen(onAction = {})
    }
}