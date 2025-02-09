package com.youllbecold.trustme.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.SelectRows
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import com.youllbecold.trustme.ui.components.generic.Tile
import com.youllbecold.trustme.ui.components.generic.TimeRangeInput
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLogForm(
    log: LogData,
    onLogChange: (LogData) -> Unit
) {
    var showTimePickerFrom by remember { mutableStateOf(false) }
    var showTimePickerTo by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var clothesBottomSheet by remember { mutableStateOf<ClothesCategory?>(null) }

    Box {
        Column(modifier = Modifier
            .padding(PADDING_AROUND_QUESTION.dp)
        ) {
            Section(title = "When were you out?",  modifier = Modifier.fillMaxWidth()) {
                TimeRangeInput(
                    fromTime = log.timeFrom,
                    toTime = log.timeTo,
                    onFromTimeClick = { showTimePickerFrom = true },
                    onToTimeClick = { showTimePickerTo = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Section(title = "How did you feel today?") {
                SelectRows(
                    items = feelingItems(),
                    onItemsSelected = { selectedItems ->
                        val result = Feeling.entries.filter { selectedItems.contains(it.ordinal) }
                        // Note: Allowed just one option.
                        onLogChange(log.copy(overallFeeling = result.first()))
                    },
                )
            }

            Section(title = "What did you wear?") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(ClothesCategory.entries.size) { index ->
                        val type = ClothesCategory.entries[index]
                        val (title, icon) = type.getUiData()

                        Tile(
                            title = title,
                            icon = icon,
                            onClick = { clothesBottomSheet = type },
                            modifier = Modifier
                                .defaultMinSize(120.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

        TimePicker(
            initial = log.timeFrom,
            onDismiss = { showTimePickerFrom = false },
            onChange = {
                onLogChange(log.copy(timeFrom = it))
                showTimePickerFrom = false
            },
            showPicker = showTimePickerFrom,
        )

        TimePicker(
            initial = log.timeTo,
            onDismiss = { showTimePickerTo = false },
            onChange = {
                onLogChange(log.copy(timeTo = it))
                showTimePickerTo = false
            },
            showPicker = showTimePickerTo,
        )

        clothesBottomSheet?.let { category ->
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { clothesBottomSheet = null },
                content = {
                    Text("Bottom sheet content for $category")

                    // TODO: Change
                    SelectRows(
                        items = listOf(
                            SelectableItemContent(R.drawable.ic_shirt, "T-shirt"),
                            SelectableItemContent(R.drawable.ic_shirt, "Long sleeve"),
                            SelectableItemContent(R.drawable.ic_shirt, "Sweater"),
                        ),
                        onItemsSelected = { selectedItems ->
                            Log.d("AddLogForm", "Selected items: $selectedItems")
                        },
                    )
                }
            )
        }
    }
}

private const val PADDING_AROUND_QUESTION = 8

@Composable
private fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        SectionTitle(
            text = title,
        )
        content()
        Spacer(modifier = Modifier.padding(PADDING_BETWEEN_SECTIONS.dp))
    }
}

private const val PADDING_BETWEEN_SECTIONS = 16

@Preview(showBackground = true)
@Composable
private fun AddLogFormPreview() {
    YoullBeColdTheme {
        AddLogForm(
            log = LogData(
                timeFrom = LocalTime.of(9, 0),
                timeTo = LocalTime.of(17, 0),
                overallFeeling = Feeling.Cold
            ),
            onLogChange = {}
        )
    }
}
