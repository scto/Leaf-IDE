package io.github.caimucheng.leaf.common.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import io.github.caimucheng.leaf.common.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

@SuppressLint("ReturnFromAwaitPointerEventScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileList(
    items: List<File>,
    fileIcon: (file: File) -> ImageVector,
    onClick: (file: File) -> Unit,
    onRename: (file: File, newName: String) -> Unit,
    onDelete: (file: File) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items.size) { index ->
            val item = items[index]
            val animatedOffset = remember {
                Animatable(Offset.Zero, Offset.VectorConverter)
            }
            var expanded by remember {
                mutableStateOf(false)
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { onClick(item) },
                            onLongClick = { expanded = true })
                        .pointerInput(Unit) {
                            coroutineScope {
                                while (true) {
                                    //获取点击位置
                                    val newOffset = awaitPointerEventScope {
                                        awaitFirstDown().position
                                    }
                                    launch {
                                        animatedOffset.animateTo(
                                            newOffset,
                                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                                        )
                                    }
                                }
                            }
                        },
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = fileIcon(item),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = item.name)
                    }
                }
                Box(modifier = Modifier.offset {
                    IntOffset(
                        animatedOffset.value.x.roundToInt(),
                        animatedOffset.value.y.roundToInt()
                    )
                }) {
                    ContextDropdownMenu(
                        expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        file = item,
                        onRename = onRename,
                        onDelete = onDelete
                    )
                }
            }

        }
    }
}

@Composable
private fun ContextDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    file: File,
    onRename: (file: File, newName: String) -> Unit,
    onDelete: (file: File) -> Unit
) {
    var showRenameFileDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showDeleteFileDialog by rememberSaveable {
        mutableStateOf(false)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxWidth = 240.dp)
    ) {
        Text(
            text = file.name,
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
                Text(text = stringResource(id = R.string.rename))
            },
            onClick = {
                showRenameFileDialog = true
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.delete))
            },
            onClick = {
                showDeleteFileDialog = true
            }
        )
    }
    if (showRenameFileDialog) {
        var name by rememberSaveable {
            mutableStateOf("")
        }
        var nameError by rememberSaveable {
            mutableStateOf("")
        }
        AlertDialog(
            title = {
                Text(text = stringResource(id = R.string.rename_file))
            },
            text = {
                val focusRequester = remember {
                    FocusRequester()
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = ""
                    },
                    label = {
                        Text(text = stringResource(id = R.string.file_name))
                    },
                    singleLine = true,
                    isError = nameError.isNotEmpty(),
                    supportingText = {
                        if (nameError.isNotEmpty()) {
                            Text(text = nameError)
                        }
                    },
                    modifier = Modifier.focusRequester(focusRequester)
                )
                SideEffect {
                    focusRequester.requestFocus()
                }
            },
            onDismissRequest = {
                showRenameFileDialog = false
            },
            confirmButton = {
                ConstraintLayout(Modifier.fillMaxWidth()) {
                    val (cancel, delete) = createRefs()
                    val fileNameCannotBeEmpty =
                        stringResource(id = R.string.file_name_cannot_be_empty)
                    val invalidFileName = stringResource(id = R.string.invalid_file_name)
                    TextButton(
                        onClick = { showRenameFileDialog = false },
                        modifier = Modifier.constrainAs(cancel) {
                            linkTo(parent.start, parent.end, bias = 0f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            if (name.isEmpty()) {
                                nameError = fileNameCannotBeEmpty
                            }

                            if ('/' in name) {
                                nameError = invalidFileName
                            }

                            if (nameError.isEmpty()) {
                                showRenameFileDialog = false
                                onDismissRequest()
                                onRename(file, name)
                            }
                        },
                        modifier = Modifier.constrainAs(delete) {
                            linkTo(cancel.end, parent.end, bias = 1f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.rename))
                    }
                }
            }
        )
    }
    if (showDeleteFileDialog) {
        AlertDialog(
            title = {
                Text(text = stringResource(id = R.string.delete_file))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_file_confirm, file.name))
            },
            onDismissRequest = {
                showDeleteFileDialog = false
            },
            confirmButton = {
                ConstraintLayout(Modifier.fillMaxWidth()) {
                    val (cancel, delete) = createRefs()
                    TextButton(
                        onClick = { showDeleteFileDialog = false },
                        modifier = Modifier.constrainAs(cancel) {
                            linkTo(parent.start, parent.end, bias = 0f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            showDeleteFileDialog = false
                            onDelete(file)
                        },
                        modifier = Modifier.constrainAs(delete) {
                            linkTo(cancel.end, parent.end, bias = 1f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                }
            }
        )
    }
}