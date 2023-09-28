package io.github.caimucheng.leaf.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.github.caimucheng.leaf.common.R
import io.github.caimucheng.leaf.common.model.FileTabItem
import io.github.caimucheng.leaf.common.model.Value

@Composable
fun FileTabs(
    items: List<FileTabItem>,
    selectedIndex: Value<Int>,
    onSelected: (index: Int) -> Unit,
    onCloseCurrent: (index: Int) -> Unit,
    onCloseOthers: (index: Int) -> Unit,
    onCloseAll: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(items.size) { index ->
            val item = items[index]
            val isSelected = index == selectedIndex.value
            var expanded by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = selectedIndex, block = {
                if (selectedIndex.value == index) {
                    onSelected(index)
                }
            })

            Column(
                Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        if (selectedIndex.value == index) {
                            expanded = true
                        }
                        onSelected(index)
                    }
            ) {
                if (expanded) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier
                            .sizeIn(maxWidth = 240.dp)
                    ) {
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.close_current))
                            },
                            onClick = {
                                onCloseCurrent(index)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.close_others))
                            },
                            onClick = {
                                onCloseOthers(index)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.close_all))
                            },
                            onClick = {
                                onCloseAll()
                                expanded = false
                            }
                        )
                    }
                }
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 15.dp)
                ) {
                    val (text, spacer) = createRefs()
                    val animatedTextColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.8f
                        ), label = "AnimateTextColor",
                        animationSpec = tween(400)
                    )
                    val animatedSpacerColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        label = "AnimatedSpacerColor",
                        animationSpec = tween(400)
                    )
                    Text(
                        text = item.name,
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(text) {
                            linkTo(parent.top, parent.bottom)
                            linkTo(parent.start, parent.end)
                        },
                        color = animatedTextColor
                    )
                    Spacer(
                        modifier = Modifier
                            .height(3.dp)
                            .background(
                                animatedSpacerColor,
                                RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                            )
                            .constrainAs(spacer) {
                                linkTo(text.bottom, parent.bottom, bias = 1f)
                                linkTo(parent.start, parent.end)
                                width = Dimension.fillToConstraints
                            }
                    )
                }
            }
        }
    }
}