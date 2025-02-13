package com.youllbecold.trustme.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.DateInput
import com.youllbecold.trustme.ui.components.generic.Section
import com.youllbecold.trustme.ui.components.generic.SelectRows
import com.youllbecold.trustme.ui.components.generic.Tile
import com.youllbecold.trustme.ui.components.generic.TimeRangeInput
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.FeelingState
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLogForm(
    date: ImmutableDate,
    timeFrom: ImmutableTime,
    timeTo: ImmutableTime,
    overallFeeling: FeelingState?,
    clothes: Set<Clothes>,
    onDateChanged: (ImmutableDate) -> Unit,
    onTimeFromChange: (ImmutableTime) -> Unit,
    onTimeToChange: (ImmutableTime) -> Unit,
    onOverallFeelingChange: (FeelingState?) -> Unit,
    onClothesCategoryChange: (Set<Clothes>) -> Unit,
    removeClothes: (Set<Clothes>) -> Unit,
    onSave: () -> Unit,
) {
    var showTimePickerFrom by remember { mutableStateOf(false) }
    var showTimePickerTo by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var clothesBottomSheet by remember { mutableStateOf<Clothes.Category?>(null) }

    val formScrollState = rememberScrollState()

    Box {
        Column(
            modifier = Modifier
                .verticalScroll(formScrollState)
                .padding(PADDING_AROUND_QUESTION.dp)
        ) {
            DateTimeSection(
                date = date,
                timeFrom = timeFrom,
                timeTo = timeTo,
                onFromTimeClick = { showTimePickerFrom = true },
                onToTimeClick = { showTimePickerTo = true },
                onDateChanged = { onDateChanged(it) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            FeelingSection(
                overallFeeling = overallFeeling,
                onOverallFeelingChange = onOverallFeelingChange
            )

            ClothesSection(
                showBottomSheet = { clothesBottomSheet = it }
            )

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_log_save))
            }
        }

        TimePicker(
            initial = timeFrom,
            onDismiss = { showTimePickerFrom = false },
            onChange = {
                onTimeFromChange(it)
                showTimePickerFrom = false
            },
            showPicker = showTimePickerFrom,
        )

        TimePicker(
            initial = timeTo,
            onDismiss = { showTimePickerTo = false },
            onChange = {
                onTimeToChange(it)
                showTimePickerTo = false
            },
            showPicker = showTimePickerTo,
        )

        clothesBottomSheet?.let { category ->
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { clothesBottomSheet = null },
                content = {
                    val preSelected = clothes.filter { it.category == category }

                    // TODO: Use real ones and preselect
                    val items = category.getItems()

                    SelectRowWithButton(
                        items = items,
                        buttonText = stringResource(R.string.add_clothes),
                        onButtonClick = { selected ->
                            val result = selected
                                .map { items[it] }
                                .toSet()

                            // TODO: onClothesCategoryChange
                            clothesBottomSheet = null
                        },
                        selected = emptyList(),
                        modifier = Modifier.padding(BOTTOM_SHEET_PADDING.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun DateTimeSection(
    date: ImmutableDate,
    timeFrom: ImmutableTime,
    timeTo: ImmutableTime,
    onDateChanged: (ImmutableDate) -> Unit,
    onFromTimeClick: () -> Unit,
    onToTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Section(
        title = stringResource(R.string.add_log_form_when),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            DateInput(
                date = date,
                onDateSelected = { onDateChanged(it) },
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(PADDING_BETWEEN_DATETIME.dp))

            TimeRangeInput(
                fromTime = timeFrom,
                toTime = timeTo,
                onFromTimeClick = onFromTimeClick,
                onToTimeClick = onToTimeClick,
            )
        }

    }
}

private const val PADDING_BETWEEN_DATETIME = 8

@Composable
private fun FeelingSection(
    overallFeeling: FeelingState?,
    onOverallFeelingChange: (FeelingState?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Section(
        title = stringResource(R.string.add_log_feeling),
        modifier = modifier
    ) {
        SelectRows(
            items = selectableFeelings(),
            preSelected = overallFeeling?.let { setOf(it.ordinal) } ?: emptySet(),
            onItemsSelected = { selectedItems ->
                val result = FeelingState.entries.filter { selectedItems.contains(it.ordinal) }

                // Note: Allowed just one option.
                onOverallFeelingChange(result.firstOrNull())
            },
        )
    }
}

@Composable
private fun ClothesSection(
    showBottomSheet: (Clothes.Category?) -> Unit,
    modifier: Modifier = Modifier
) {
    val clothesScrollState = rememberScrollState()

    Section(
        title = stringResource(R.string.add_log_wearing),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.horizontalScroll(clothesScrollState),
        ){
            Clothes.Category.entries.forEach { type ->
                val (title, icon) = type.getUiData()

                Tile(
                    title = title,
                    icon = icon,
                    onClick = { showBottomSheet(type) },
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
    }
}

private const val PADDING_AROUND_QUESTION = 8
private const val BOTTOM_SHEET_PADDING = 12

@Preview(showBackground = true)
@Composable
private fun AddLogFormPreview() {
    YoullBeColdTheme {
        AddLogForm(
            date = ImmutableDate(LocalDate.now()),
            timeFrom = ImmutableTime(LocalTime.now()),
            timeTo = ImmutableTime(LocalTime.now()),
            overallFeeling = null,
            clothes = emptySet(),
            onDateChanged = { },
            onTimeFromChange = { },
            onTimeToChange = { },
            onOverallFeelingChange = { },
            onClothesCategoryChange = { },
            removeClothes = { },
            onSave = { }
        )
    }
}
