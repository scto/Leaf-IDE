package io.github.caimucheng.leaf.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SymbolTabLayout(
    modifier: Modifier = Modifier,
    items: Set<String>,
    useSpaceInsteadOfTab: Boolean,
    onInsert: (text: String) -> Unit
) {
    val listItems = items.toList()
    LazyRow(modifier = modifier) {
        item {
            SymbolItem(
                text = "→",
                insertText = if (useSpaceInsteadOfTab) " ".repeat(4) else "\t",
                onInsert = onInsert
            )
        }
        items(listItems.size) { index ->
            val item = listItems[index]
            SymbolItem(
                text = item,
                insertText = item,
                onInsert = onInsert
            )
        }
    }
}

@Composable
private fun SymbolItem(
    text: String,
    insertText: String,
    onInsert: (text: String) -> Unit
) {
    Column(
        modifier = Modifier
            .defaultMinSize(minWidth = 40.dp)
            .fillMaxHeight()
            .clickable {
                onInsert(insertText)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
        )
    }
}