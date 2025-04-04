package com.youllbecold.trustme.common.ui.components.datetime

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.icontext.ClickableText
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.themed.ThemedButton
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    date: DateState,
    onDateSelected: (DateState) -> Unit,
    modifier: Modifier = Modifier,
    allowFuture: Boolean = false,
) {
    val context = LocalContext.current

    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val todayMillis = remember { System.currentTimeMillis() }

    val selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return allowFuture || utcTimeMillis <= todayMillis // Only allow today and past dates
        }

        override fun isSelectableYear(year: Int): Boolean {
            return allowFuture || year <= ZonedDateTime.now().year
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.toMillis(),
        selectableDates = selectableDates
    )

    ClickableText(
        text = date.formatDate(),
        iconType = IconType.Calendar,
        onClick = { showDatePicker = true },
        modifier = modifier,
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                ThemedButton(stringResource(R.string.dialog_ok)) {
                    val dateMillis = datePickerState.selectedDateMillis

                    if (dateMillis != null) {
                        val localDate = Instant.ofEpochMilli(dateMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        onDateSelected(
                            DateState(
                                year = localDate.year,
                                month = localDate.monthValue,
                                day = localDate.dayOfMonth
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_select_date),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    showDatePicker = false
                }
            },
            dismissButton = {
                ThemedButton(stringResource(R.string.dialog_dismiss)) {
                    showDatePicker = false
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DateInputPreview() {
    YoullBeColdTheme {
        DateInput(
            date = DateState(2022, 1, 1),
            onDateSelected = { }
        )
    }
}
