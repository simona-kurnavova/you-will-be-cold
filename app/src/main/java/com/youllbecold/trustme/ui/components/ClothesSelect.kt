package com.youllbecold.trustme.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.SelectRows
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent

@Composable
fun ClothesSelect(
    modifier: Modifier = Modifier,
    category: ClothesCategory,
    selected: List<Int> = emptyList(),
    onSave: (List<Int>) -> Unit = {},
) {
    val items = getItems(category)
    var selectedState by rememberSaveable { mutableStateOf(selected) }

    Column(
        modifier = modifier
    ) {
        SelectRows(
            items = items,
            onItemsSelected = { selectedItems ->
                selectedState = selectedItems
            },
            allowMultipleSelection = true,
            maxSelectedItems = items.size,
            preSelected = selected.toSet(),
            modifier = modifier,
        )

        Spacer(modifier = Modifier.padding(SPACE_BETWEEN_ITEMS.dp))

        Button(
            onClick = { onSave(selectedState) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_clothes))
        }
    }
}

private const val SPACE_BETWEEN_ITEMS = 8

private fun getItems(category: ClothesCategory): List<SelectableItemContent> = listOf(
    SelectableItemContent(R.drawable.ic_shirt, "$category T-shirt"),
    SelectableItemContent(R.drawable.ic_shirt, "$category Long sleeve"),
    SelectableItemContent(R.drawable.ic_shirt, "$category  Sweater"),
)

@Preview(showBackground = true)
@Composable
fun ClothesSelectPreview() {
    ClothesSelect(
        category = ClothesCategory.Top,
        selected = listOf(0, 1),
    )
}