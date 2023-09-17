package leaf.plugin.nodejs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.caimucheng.leaf.plugin.PluginProject

class NodeJSPluginProject : PluginProject() {

    override fun getDisplayedPictureResId(): Int {
        return R.mipmap.displayed_nodejs_picture
    }

    override fun getDisplayedTitleId(): Int {
        return R.string.displayed_nodejs_title
    }

    override fun getDisplayedProjectLogoResId(): Int {
        return R.drawable.nodejs_logo
    }

    override fun getDisplayedProjectTitleId(): Int {
        return R.string.displayed_project_title
    }

    @Composable
    override fun CreateProjectDialog(onDismissRequest: () -> Unit, onNavigateHome: () -> Unit) {
        var name by rememberSaveable {
            mutableStateOf("")
        }
        var description by rememberSaveable {
            mutableStateOf("")
        }
        var version by rememberSaveable {
            mutableStateOf("")
        }
        var author by rememberSaveable {
            mutableStateOf("")
        }
        var showMore by rememberSaveable {
            mutableStateOf(false)
        }
        val angle by animateFloatAsState(
            targetValue = if (showMore) 180f else 0f,
            label = "angle",
            animationSpec = tween(220)
        )

        val projectNameCannotBeEmpty = stringResource(R.string.project_name_cannot_be_empty)
        val invalidProjectName = stringResource(R.string.invalid_project_name)
        val descriptionCannotBeEmpty = stringResource(R.string.project_description_cannot_be_empty)

        var nameError by rememberSaveable {
            mutableStateOf("")
        }
        var descriptionError by rememberSaveable {
            mutableStateOf("")
        }

        val onClick = {
            if (name.isEmpty()) {
                nameError = projectNameCannotBeEmpty
            }

            if ('/' in name) {
                nameError = invalidProjectName
            }

            if (description.isEmpty()) {
                descriptionError = descriptionCannotBeEmpty
            }

            if (nameError.isEmpty() && descriptionError.isEmpty()) {
                onDismissRequest()
            }
        }
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = stringResource(id = R.string.displayed_nodejs_title))
            },
            text = {
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = ""
                        },
                        label = {
                            Text(text = stringResource(R.string.project_name))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.SdCard,
                                contentDescription = stringResource(R.string.project_name),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        isError = nameError.isNotEmpty(),
                        supportingText = {
                            if (nameError.isNotEmpty()) {
                                Text(text = nameError)
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            descriptionError = ""
                        },
                        label = {
                            Text(text = stringResource(R.string.project_description))
                        },
                        isError = descriptionError.isNotEmpty(),
                        supportingText = {
                            if (descriptionError.isNotEmpty()) {
                                Text(text = descriptionError)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = stringResource(R.string.project_description),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = if (showMore) ImeAction.Next else ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            // performClick
                            onClick()
                        }),
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconToggleButton(checked = showMore, onCheckedChange = {
                            showMore = it
                        }) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.more),
                                modifier = Modifier.graphicsLayer {
                                    rotationZ = angle
                                }
                            )
                        }
                    }
                    if (showMore) {
                        Spacer(modifier = Modifier.height(15.dp))
                        More(
                            version = version,
                            onVersionValueChange = {
                                version = it
                            },
                            author = author,
                            onAuthorValueChange = {
                                author = it
                            },
                            onClick = { onClick() }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onClick() }) {
                    Text(text = stringResource(id = R.string.create))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismissRequest()
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

    @Composable
    private fun More(
        version: String,
        onVersionValueChange: (String) -> Unit,
        author: String,
        onAuthorValueChange: (String) -> Unit,
        onClick: () -> Unit
    ) {
        OutlinedTextField(
            value = version,
            onValueChange = onVersionValueChange,
            label = {
                Text(text = stringResource(R.string.version))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(R.string.version),
                    modifier = Modifier.size(20.dp)
                )
            },
            supportingText = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )
        OutlinedTextField(
            value = author,
            onValueChange = onAuthorValueChange,
            label = {
                Text(text = stringResource(R.string.author))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.SdCard,
                    contentDescription = stringResource(R.string.author),
                    modifier = Modifier.size(20.dp)
                )
            },
            supportingText = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = {
                // performClick
                onClick()
            }),
            singleLine = true
        )
    }

}