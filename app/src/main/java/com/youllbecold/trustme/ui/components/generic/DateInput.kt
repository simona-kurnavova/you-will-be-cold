package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.formatDate
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    date: ImmutableDate,
    onDateSelected: (ImmutableDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val context = LocalContext.current

    ClickableText(
        text = date.date.formatDate(),
        painter = rememberVectorPainter(Icons.Default.DateRange),
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

                        onDateSelected(ImmutableDate(localDate))
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
            }
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
            date = ImmutableDate(LocalDate.now()),
            onDateSelected = { }
        )
    }
}
