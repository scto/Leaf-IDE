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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.component.LoadingDialog
import io.github.caimucheng.leaf.common.icon.NodeFile
import io.github.caimucheng.leaf.common.model.Workspace
import io.github.caimucheng.leaf.common.util.LeafIDEProjectPath
import io.github.caimucheng.leaf.plugin.PluginProject
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

class NodeJSPluginProject : PluginProject() {

    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("NodeJSPluginCoroutine"))

    private val lock = Mutex()

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

    override fun getFileIcon(file: File): ImageVector? {
        if (file.name == "package.json" && file.isFile) {
            return Icons.Filled.NodeFile
        }
        return super.getFileIcon(file)
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
        var version by rememberSaveable {
            mutableStateOf("1.0")
        }
        var author by rememberSaveable {
            mutableStateOf("User")
        }
        var nameError by rememberSaveable {
            mutableStateOf("")
        }
        var descriptionError by rememberSaveable {
            mutableStateOf("")
        }
        val loadingTitle: MutableState<String?> = remember {
            mutableStateOf(null)
        }
        val showLoading = remember {
            mutableStateOf(false)
        }

        val projectNameCannotBeEmpty = stringResource(R.string.project_name_cannot_be_empty)
        val invalidProjectName = stringResource(R.string.invalid_project_name)
        val existsProject = stringResource(R.string.exists_project)
        val descriptionCannotBeEmpty = stringResource(R.string.project_description_cannot_be_empty)
        val createProjectFailed = stringResource(R.string.create_project_failed)

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

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
                showLoading.value = true
                launchCreateProjectCoroutine(
                    showLoading,
                    loadingTitle,
                    name,
                    description,
                    version,
                    author,
                    onError = { exception ->
                        exception.printStackTrace()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                String.format(
                                    createProjectFailed,
                                    exception.message
                                ),
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                        }
                    },
                    backHome = backHome
                )
            }
        }

        LeafApp(
            title = stringResource(R.string.displayed_nodejs_title),
            snackbarHost = { SnackbarHost(snackbarHostState) },
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
                if (showLoading.value) {
                    LoadingDialog(title = loadingTitle.value)
                }
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {

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

    private fun launchCreateProjectCoroutine(
        showLoading: MutableState<Boolean>,
        loadingTitle: MutableState<String?>,
        name: String,
        description: String,
        version: String,
        author: String,
        onError: (e: Exception) -> Unit,
        backHome: () -> Unit
    ) {
        coroutineScope.launch {
            lock.lock()
            try {
                createProject(
                    loadingTitle,
                    name,
                    description,
                    version,
                    author
                )
                refresh()
                backHome()
            } catch (e: Exception) {
                onError(e)
            } finally {
                showLoading.value = false
                lock.unlock()
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private suspend fun createProject(
        loadingTitle: MutableState<String?>,
        name: String,
        description: String,
        version: String,
        author: String
    ) {
        return withContext(Dispatchers.IO) {
            val rootFile = File(LeafIDEProjectPath, name)
            rootFile.mkdirs()

            val configurationDirectory = File(rootFile, ".LeafIDE")
            configurationDirectory.mkdirs()

            val workspaceFile = File(configurationDirectory, "workspace.xml")
            Workspace(
                name,
                description,
                getPackageName(),
                emptyMap()
            ).storeToXML(FileOutputStream(workspaceFile))

            val packageFile = File(rootFile, "package.json")
            val jsonObject = JSONObject()
            jsonObject.put("name", name)
            jsonObject.put("version", version)
            jsonObject.put("description", description)
            jsonObject.put("main", "index.js")
            jsonObject.put("scripts", JSONObject().apply {
                put("start", "node index.js")
            })
            jsonObject.put("keywords", JSONArray())
            jsonObject.put("author", author)
            jsonObject.put("license", "ISU")
            FileWriter(packageFile).use {
                it.write(jsonObject.toString(2))
                it.flush()
            }

            File(rootFile, "index.js").createNewFile()
        }
    }

    private suspend fun MutableState<String?>.setValue(value: String?) {
        return withContext(Dispatchers.Main) {
            this@setValue.value = value
        }
    }

}