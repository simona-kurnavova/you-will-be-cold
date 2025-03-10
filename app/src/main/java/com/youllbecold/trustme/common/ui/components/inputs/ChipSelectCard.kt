package com.youllbecold.trustme.common.ui.components.inputs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.components.themed.ThemedChip
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun ChipSelectCard(
    options: PersistentList<String>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: Int = 0,
    horizontalPadding: Int = 0,
    content: @Composable (Int) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = selectedOption,
        pageCount = options::size
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onOptionSelected(pagerState.currentPage) // Pager state changed
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding.dp),
            horizontalArrangement = Arrangement.spacedBy(SPACE_BETWEEN_CHIPS.dp)
        ) {
            options.forEachIndexed { index, option ->
                ThemedChip(
                    text = option,
                    onClick = {
                        onOptionSelected(index)

                        // Scroll to the clicked tab in the pager
                        scope.launch { pagerState.scrollToPage(index) }
                    },
                    selected = index == selectedOption,
                )
            }
        }
        
        Spacer(modifier = Modifier.height(PADDING_UNDER_CHIPS.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = (horizontalPadding - PADDING_BETWEEN_PAGES).dp),
            verticalAlignment = Alignment.Top,
        ) { page ->
            Box(modifier = Modifier.padding(horizontal = PADDING_BETWEEN_PAGES.dp)) {
                content(page)
            }
        }
    }
}

private const val SPACE_BETWEEN_CHIPS = 4
private const val PADDING_UNDER_CHIPS = 4
private const val PADDING_BETWEEN_PAGES = 4

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ChipSelectCardPreview() {
    YoullBeColdTheme {
        ChipSelectCard(
            options = persistentListOf("Option 1", "Option 2", "Option 3"),
            onOptionSelected = {},
            selectedOption = 1,
            content = {
                Text("Content")
            }
        )
    }
}
