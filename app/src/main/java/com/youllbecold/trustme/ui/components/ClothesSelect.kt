package com.youllbecold.trustme.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.youllbecold.trustme.ui.components.generic.ThemedButton

@Composable
fun ClothesSelect(
    items: List<SelectableItemContent>,
    modifier: Modifier = Modifier,
    selected: List<Int> = emptyList(),
    onSave: (List<Int>) -> Unit = {},
) {
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
        )

        Spacer(modifier = Modifier.width(SPACE_BETWEEN_ITEMS.dp))

        ThemedButton(
            text = stringResource(R.string.add_clothes),
            onClick = { onSave(selectedState) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private const val SPACE_BETWEEN_ITEMS = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ClothesSelectPreview() {
    ClothesSelect(
        items = listOf(
                SelectableItemContent(R.drawable.ic_shirt, "T-shirt"),
                SelectableItemContent(R.drawable.ic_shirt, "Long sleeve"),
                SelectableItemContent(R.drawable.ic_shirt, "Sweater"),
            ),
        selected = listOf(0, 1),
    )
}
