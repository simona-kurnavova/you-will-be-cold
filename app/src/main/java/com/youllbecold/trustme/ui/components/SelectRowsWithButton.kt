package com.youllbecold.trustme.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.SelectRows
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import com.youllbecold.trustme.ui.components.generic.ThemedButton

@Composable
fun SelectRowWithButton(
    items: List<SelectableItemContent>,
    buttonText: String,
    onButtonClick: (List<Int>) -> Unit,
    modifier: Modifier = Modifier,
    selected: List<Int> = emptyList(),
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

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

        ThemedButton(
            text = buttonText,
            onClick = { onButtonClick(selectedState) },
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
        items = listOf(
                SelectableItemContent(R.drawable.ic_shirt, "T-shirt"),
                SelectableItemContent(R.drawable.ic_shirt, "Long sleeve"),
                SelectableItemContent(R.drawable.ic_shirt, "Sweater"),
            ),
        buttonText = "Add clothes",
        onButtonClick = {},
    )
}
