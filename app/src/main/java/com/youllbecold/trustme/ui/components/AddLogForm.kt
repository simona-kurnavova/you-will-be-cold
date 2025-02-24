package com.youllbecold.trustme.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.datetime.DateInput
import com.youllbecold.trustme.ui.components.generic.inputs.LabeledSlider
import com.youllbecold.trustme.ui.components.generic.Section
import com.youllbecold.trustme.ui.components.generic.inputs.SelectRowWithButton
import com.youllbecold.trustme.ui.components.generic.ThemedButton
import com.youllbecold.trustme.ui.components.generic.ThemedInputChip
import com.youllbecold.trustme.ui.components.generic.icontext.Tile
import com.youllbecold.trustme.ui.components.generic.datetime.TimeRangeInput
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.utils.getFeelingWithLabel
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.utils.items
import com.youllbecold.trustme.ui.utils.getTitle
import com.youllbecold.trustme.ui.utils.toSelectableItemContent
import com.youllbecold.trustme.ui.utils.withCategory
import com.youllbecold.trustme.ui.viewmodels.FeelingState
import com.youllbecold.trustme.ui.viewmodels.FeelingsState
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddLogForm(
    date: ImmutableDate,
    timeFrom: ImmutableTime,
    timeTo: ImmutableTime,
    feelings: FeelingsState,
    clothes: PersistentSet<Clothes>,
    onDateChanged: (ImmutableDate) -> Unit,
    onTimeFromChange: (ImmutableTime) -> Unit,
    onTimeToChange: (ImmutableTime) -> Unit,
    onFeelingsChange: (FeelingsState) -> Unit,
    onClothesCategoryChange: (PersistentSet<Clothes>) -> Unit,
    onSave: () -> Unit,
) {
    val context = LocalContext.current
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
                onFromTimeSelected = { onTimeFromChange(it) },
                onToTimeSelected = { onTimeToChange(it) },
                onDateChanged = { onDateChanged(it) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            ClothesSection(
                clothes = clothes,
                onClothesCategoryChange = onClothesCategoryChange,
            )

            FeelingSection(
                feelings = feelings,
                onFeelingsChange = onFeelingsChange
            )

            ThemedButton(
                text = stringResource(R.string.add_log_save),
                onClick = {
                    onSave()
                },
                modifier = Modifier.fillMaxWidth()
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
    onFromTimeSelected: (ImmutableTime) -> Unit,
    onToTimeSelected: (ImmutableTime) -> Unit,
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
            )

            Spacer(modifier = Modifier.height(PADDING_BETWEEN_DATETIME.dp))

            TimeRangeInput(
                fromTime = timeFrom,
                toTime = timeTo,
                onFromTimeSelected = onFromTimeSelected,
                onToTimeSelected = onToTimeSelected,
            )
        }

    }
}

private const val PADDING_BETWEEN_DATETIME = 8

@Composable
private fun FeelingSection(
    feelings: FeelingsState,
    onFeelingsChange: (FeelingsState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Section(
        title = stringResource(R.string.add_log_feeling),
        modifier = modifier
    ) {
        val options = FeelingState.entries.toSelectableItemContent().map { it.title }

        Column {
            feelings.getFeelingWithLabel(onFeelingsChange)
                .forEach {
                    LabeledSlider(
                        label = it.label,
                        options = options,
                        selected = it.feeling.ordinal,
                        onSelected = { ordinal ->
                            it.update(FeelingState.entries.first { it.ordinal == ordinal })
                        },
                        modifier = Modifier.padding(SLIDER_PADDING.dp)
                    )

                    Spacer(modifier = Modifier.height(SPACER_BETWEEN_SLIDERS.dp))
                }
        }
    }
}

private const val SPACER_BETWEEN_SLIDERS = 12
private const val SLIDER_PADDING = 8

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ClothesSection(
    clothes: PersistentSet<Clothes>,
    onClothesCategoryChange: (PersistentSet<Clothes>) -> Unit,
    modifier: Modifier = Modifier
) {
    val clothesScrollState = rememberScrollState()
    var clothesBottomSheet by remember { mutableStateOf<Clothes.Category?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Section(
        title = stringResource(R.string.add_log_wearing),
        modifier = modifier
    ) {
        Column {
            FlowRow {
                // Selected clothes chips
                clothes.forEach { item ->
                    key(item) {
                        ThemedInputChip(
                            text = item.getTitle(),
                            iconType = item.icon,
                            onRemove = { onClothesCategoryChange(clothes.remove(item)) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }

            if (clothes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(SPACER_BETWEEN_SLIDERS.dp))
            }

            Row(
                modifier = Modifier.horizontalScroll(clothesScrollState),
            ){
                // All categories
                Clothes.Category.entries.forEach { type ->
                    Tile(
                        title = type.getTitle(),
                        iconType = type.icon,
                        onClick = { clothesBottomSheet = type },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }

    clothesBottomSheet?.let { category ->
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { clothesBottomSheet = null },
            content = {
                val allClothesInCategory = category.items

                val preSelected = clothes
                    .withCategory(category)
                    .map { allClothesInCategory.indexOf(it) }

                SelectRowWithButton(
                    items = allClothesInCategory.toSelectableItemContent(),
                    buttonText = stringResource(R.string.add_clothes),
                    onButtonClick = { selected ->
                        val newSelection = selected.map { allClothesInCategory[it] }
                        onClothesCategoryChange(
                            clothes
                                .removeAll(allClothesInCategory)
                                .addAll(newSelection)
                        )
                        clothesBottomSheet = null
                    },
                    selected = preSelected,
                    modifier = Modifier.padding(BOTTOM_SHEET_PADDING.dp)
                )
            }
        )
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
            feelings = FeelingsState(),
            clothes = persistentSetOf(Clothes.JEANS, Clothes.SHORT_TSHIRT_DRESS, Clothes.SHORTS, Clothes.SHORT_SKIRT),
            onDateChanged = { },
            onTimeFromChange = { },
            onTimeToChange = { },
            onFeelingsChange = { },
            onClothesCategoryChange = { },
            onSave = { }
        )
    }
}
