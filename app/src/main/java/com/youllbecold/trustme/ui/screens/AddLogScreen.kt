package com.youllbecold.trustme.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.components.AddLogForm
import com.youllbecold.trustme.ui.components.LogData
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime

@Composable
fun AdLogRoot() {
    AddLogScreen()
}

@Composable
fun AddLogScreen(
) {
    val currentTime = LocalTime.now()

    var logData by remember {
        mutableStateOf(
            LogData(
                timeFrom = currentTime,
                timeTo = currentTime,
                overallFeeling = null
            )
        )
    }

    AddLogForm(logData) { newLog ->
        Log.d("AddLogScreen", "New log: $newLog")
        logData = newLog
    }
}

@Preview(showBackground = true)
@Composable
fun AdLogScreenPreview() {
    YoullBeColdTheme {
        AddLogScreen()
    }
}