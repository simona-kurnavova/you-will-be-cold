package com.youllbecold.trustme.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.components.AddLogFormWrapper
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.AddLogAction
import com.youllbecold.trustme.ui.viewmodels.AddLogViewModel
import com.youllbecold.trustme.ui.viewmodels.LogState
import org.koin.androidx.compose.koinViewModel
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

    val defaultLog = LogState(
        timeFrom = currentTime.minusHours(1),
        timeTo = currentTime,
        overallFeeling = null,
        clothes = emptySet()
    )

    AddLogFormWrapper(
        onSave = { onAction(AddLogAction.SaveLog(it)) },
        log = defaultLog
    )
}

@Preview(showBackground = true)
@Composable
fun AdLogScreenPreview() {
    YoullBeColdTheme {
        AddLogScreen(onAction = {})
    }
}