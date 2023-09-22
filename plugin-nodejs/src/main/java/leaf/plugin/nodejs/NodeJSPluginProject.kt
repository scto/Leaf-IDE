package leaf.plugin.nodejs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.util.LeafIDEProjectPath
import io.github.caimucheng.leaf.plugin.PluginProject
import java.io.File

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun CreateProjectScreen(back: () -> Unit, backHome: () -> Unit) {
        var name by rememberSaveable {
            mutableStateOf("NodeJSProject")
        }
        var description by rememberSaveable {
            mutableStateOf("My NodeJS Project")
        }
        var nameError by rememberSaveable {
            mutableStateOf("")
        }
        var descriptionError by rememberSaveable {
            mutableStateOf("")
        }

        val projectNameCannotBeEmpty = stringResource(R.string.project_name_cannot_be_empty)
        val invalidProjectName = stringResource(R.string.invalid_project_name)
        val existsProject = stringResource(R.string.exists_project)
        val descriptionCannotBeEmpty = stringResource(R.string.project_description_cannot_be_empty)

        val onClick = {
            if (name.isEmpty()) {
                nameError = projectNameCannotBeEmpty
            }

            if ('/' in name) {
                nameError = invalidProjectName
            }

            if (File(LeafIDEProjectPath, name).exists()) {
                nameError = existsProject
            }

            if (description.isEmpty()) {
                descriptionError = descriptionCannotBeEmpty
            }

            if (nameError.isEmpty() && descriptionError.isEmpty()) {
                backHome()
            }
        }

        LeafApp(
            title = stringResource(R.string.displayed_nodejs_title),
            navigationIcon = {
                IconButton(onClick = {
                    back()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            },
            content = {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {
                    var version by rememberSaveable {
                        mutableStateOf("1.0")
                    }
                    var author by rememberSaveable {
                        mutableStateOf("User")
                    }

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { value ->
                                name = value
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
                                    Text(
                                        text = nameError,
                                        modifier = Modifier.padding(bottom = 15.dp)
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { value ->
                                description = value
                                descriptionError = ""
                            },
                            label = {
                                Text(text = stringResource(R.string.project_description))
                            },
                            isError = descriptionError.isNotEmpty(),
                            supportingText = {
                                if (descriptionError.isNotEmpty()) {
                                    Text(
                                        text = descriptionError,
                                        modifier = Modifier.padding(bottom = 15.dp)
                                    )
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Description,
                                    contentDescription = stringResource(R.string.project_description),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = version,
                            onValueChange = { value ->
                                version = value
                            },
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
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = author,
                            onValueChange = { value ->
                                author = value
                            },
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
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            bottomBar = {
                BottomAppBar {
                    OutlinedButton(
                        onClick = {
                            back()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 20.dp)
                            .fillMaxHeight()
                            .padding(top = 15.dp, bottom = 15.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.back)
                        )
                    }
                    Spacer(modifier = Modifier.width(90.dp))
                    Button(
                        onClick = {
                            onClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 20.dp)
                            .fillMaxHeight()
                            .padding(top = 15.dp, bottom = 15.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.create)
                        )
                    }

                }
            }
        )
    }

}