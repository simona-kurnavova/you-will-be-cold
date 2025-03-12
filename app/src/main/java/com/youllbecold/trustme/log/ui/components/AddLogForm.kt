package com.youllbecold.trustme.log.ui.components

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.datetime.DateTimeInput
import com.youllbecold.trustme.common.ui.components.icontext.Tile
import com.youllbecold.trustme.common.ui.components.inputs.LabeledSlider
import com.youllbecold.trustme.common.ui.components.inputs.SelectRowWithButton
import com.youllbecold.trustme.common.ui.components.inputs.SelectableItemContent
import com.youllbecold.trustme.common.ui.components.section.Section
import com.youllbecold.trustme.common.ui.components.themed.ThemedButton
import com.youllbecold.trustme.common.ui.components.themed.ThemedInputChip
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.mappers.getAllItems
import com.youllbecold.trustme.common.ui.mappers.getAllItemsAsSet
import com.youllbecold.trustme.common.ui.mappers.withCategory
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import com.youllbecold.trustme.common.ui.model.clothes.ClothesCategory
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.log.ui.model.FeelingState
import com.youllbecold.trustme.log.ui.model.FeelingsState
import com.youllbecold.trustme.log.ui.model.mappers.clothesName
import com.youllbecold.trustme.log.ui.model.mappers.getFeelingWithLabel
import com.youllbecold.trustme.log.ui.model.mappers.icon
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentList

@Composable
fun AddLogForm(
    dateTimeState: DateTimeState,
    feelings: FeelingsState,
    clothes: PersistentSet<Clothes>,
    onDateTimeChanged: (DateTimeState) -> Unit,
    onFeelingsChange: (FeelingsState) -> Unit,
    onClothesCategoryChange: (PersistentSet<Clothes>) -> Unit,
    onSave: () -> Unit,
    isSaving: Boolean,
    modifier: Modifier = Modifier
) {
    val formScrollState = rememberScrollState()

    Box(
       modifier = modifier.padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(formScrollState)
                .padding(PADDING_AROUND_QUESTION.dp)
        ) {
            DateTimeSection(
                dateTimeState = dateTimeState,
                onDateTimeChanged = onDateTimeChanged,
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

            if (isSaving) {
                // If saving state show loading instead of button
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // Otherwise show button to save
                ThemedButton(
                    text = stringResource(R.string.add_log_save),
                    onClick = {
                        onSave()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(PADDING_BOTTOM.dp))
        }
    }
}

private const val PADDING_BOTTOM = 8
private const val HORIZONTAL_SCREEN_PADDING = 16

@Composable
private fun DateTimeSection(
    dateTimeState: DateTimeState,
    onDateTimeChanged: (DateTimeState) -> Unit,
    modifier: Modifier = Modifier
) {
    Section(
        title = stringResource(R.string.add_log_form_when),
        modifier = modifier.fillMaxWidth()
    ) {
        DateTimeInput(
            dateTimeState = dateTimeState,
            onDatetimeChanged = onDateTimeChanged,
        )
    }
}

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
        val options = FeelingState.entries
            .toSelectableItemContent()
            .map { it.title }
            .toPersistentList()

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
    var showForCategory by remember { mutableStateOf<ClothesCategory?>(null) }

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
                            text = stringResource(item.name),
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
                ClothesCategory.getAll().forEach { type ->
                    Tile(
                        title = stringResource(type.name),
                        iconType = type.icon,
                        onClick = { showForCategory = type },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }

    ClothesBottomSheetPicker(
        clothes = clothes,
        onClothesCategoryChange = onClothesCategoryChange,
        showForCategory = showForCategory,
        closeDialog = { showForCategory = null }
    )
}

private const val PADDING_AROUND_QUESTION = 8
private const val BOTTOM_SHEET_PADDING = 12

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClothesBottomSheetPicker(
    clothes: PersistentSet<Clothes>,
    onClothesCategoryChange: (PersistentSet<Clothes>) -> Unit,
    showForCategory: ClothesCategory?,
    closeDialog: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    showForCategory?.let { category ->
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { closeDialog() },
            content = {
                val allClothesInCategory = category.getAllItems()

                val preSelected = clothes
                    .withCategory(category)
                    .map { allClothesInCategory.indexOf(it) }
                    .toPersistentList()

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
                        closeDialog()
                    },
                    selected = preSelected,
                    modifier = Modifier.padding(BOTTOM_SHEET_PADDING.dp)
                )
            }
        )
    }
}

/**
 * Returns a list of [SelectableItemContent] for the feelings state.
 */
@Composable
private fun List<FeelingState>.toSelectableItemContent(): List<SelectableItemContent> = map { feeling ->
    SelectableItemContent(
        iconType = feeling.icon,
        title = feeling.clothesName(),
    )
}


/**
 * Converts a set of [Clothes] to a list of [SelectableItemContent].
 */
@Composable
private fun List<Clothes>.toSelectableItemContent(): PersistentList<SelectableItemContent> =
    this.map { item ->
        SelectableItemContent(
            title = stringResource(item.name),
            iconType = item.icon,
        )
    }.toPersistentList()

@Preview(showBackground = true)
@Composable
private fun AddLogFormPreview() {
    YoullBeColdTheme {
        AddLogForm(
            dateTimeState = DateTimeState(
                date = DateState(2022, 1, 1),
                timeFrom = TimeState(12, 0),
                timeTo = TimeState(13, 0)
            ),
            feelings = FeelingsState(),
            clothes = ClothesCategory.getAll().first().getAllItemsAsSet(),
            onDateTimeChanged = { },
            onFeelingsChange = { },
            onClothesCategoryChange = { },
            onSave = { },
            isSaving = true
        )
    }
}
