package io.github.caimucheng.leaf.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.caimucheng.leaf.common.model.BreadcrumbItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Breadcrumb(
    items: List<BreadcrumbItem>,
    selectedIndex: Int,
    onItemClick: (index: Int) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        state = state
    ) {
        items(items.size) { index ->
            val item = items[index]
            val selectedColor by animateColorAsState(
                targetValue = if (index == selectedIndex) MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.8f
                ) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                label = "animateSelectedColor"
            )

            if (index > 0) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    ),
                    modifier = Modifier.size(24.dp)
                )
            }

            Card(
                onClick = { onItemClick(index) },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = item.file.name,
                    color = selectedColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}