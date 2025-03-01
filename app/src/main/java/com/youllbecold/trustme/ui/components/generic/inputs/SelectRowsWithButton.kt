package com.youllbecold.trustme.ui.components.generic.inputs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.ThemedButton
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet

@Composable
fun SelectRowWithButton(
    items: PersistentList<SelectableItemContent>,
    buttonText: String,
    onButtonClick: (PersistentList<Int>) -> Unit,
    modifier: Modifier = Modifier,
    selected: PersistentList<Int> = persistentListOf(),
) {
    var selectedState = remember { mutableStateOf(selected) }

    Column(
        modifier = modifier
    ) {
        SelectRows(
            items = items,
            onItemsSelected = { selectedItems ->
                selectedState.value = selectedItems
            },
            allowMultipleSelection = true,
            maxSelectedItems = items.size,
            preSelected = selected.toPersistentSet(),
        )

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

        ThemedButton(
            text = buttonText,
            onClick = { onButtonClick(selectedState.value.toPersistentList()) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private const val SPACE_BETWEEN_ITEMS = 12

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SelectRowWithButtonPreview() {
    SelectRowWithButton(
        items = persistentListOf(
                SelectableItemContent(IconType.Hat, "T-shirt"),
                SelectableItemContent(IconType.TShirt, "Long sleeve"),
                SelectableItemContent(IconType.Dress, "Sweater"),
            ),
        buttonText = "Choose clothes",
        onButtonClick = {},
    )
}
