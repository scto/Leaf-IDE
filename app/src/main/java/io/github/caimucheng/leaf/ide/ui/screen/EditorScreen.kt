package io.github.caimucheng.leaf.ide.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.caimucheng.leaf.common.component.Breadcrumb
import io.github.caimucheng.leaf.common.component.CodeEditor
import io.github.caimucheng.leaf.common.component.FileList
import io.github.caimucheng.leaf.common.component.FileTabs
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.component.LoadingDialog
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.BreadcrumbItem
import io.github.caimucheng.leaf.common.model.PreferenceRequest
import io.github.caimucheng.leaf.common.util.DisplayConfigurationDirKey
import io.github.caimucheng.leaf.common.util.SettingsDataStore
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.application.AppContext
import io.github.caimucheng.leaf.ide.application.appViewModel
import io.github.caimucheng.leaf.ide.component.Loading
import io.github.caimucheng.leaf.ide.manager.IconManager
import io.github.caimucheng.leaf.ide.manager.ProjectManager
import io.github.caimucheng.leaf.ide.model.Plugin
import io.github.caimucheng.leaf.ide.model.Project
import io.github.caimucheng.leaf.ide.viewmodel.AppUIIntent
import io.github.caimucheng.leaf.ide.viewmodel.AppUIState
import io.github.caimucheng.leaf.ide.viewmodel.EditorUIIntent
import io.github.caimucheng.leaf.ide.viewmodel.EditorViewModel
import io.github.caimucheng.leaf.plugin.PluginProject
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.event.EventReceiver
import io.github.rosemoe.sora.text.Content
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

@Composable
fun EditorScreen(
    pageNavController: NavController,
    packageName: String,
    path: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        var isLoading by rememberSaveable {
            mutableStateOf(appViewModel.state.value !is AppUIState.Done)
        }
        var plugin: Plugin? by remember {
            mutableStateOf(null)
        }
        var project: Project? by remember {
            mutableStateOf(null)
        }

        Crossfade(
            targetState = isLoading,
            label = "CrossfadeLoadingEditor",
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (it) {
                Loading()
            } else {
                if (plugin == null || plugin?.project == null || project == null) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(id = R.string.unable_to_load_editor_ui))
                        Spacer(modifier = Modifier.height(15.dp))
                        FilledIconButton(onClick = {
                            pageNavController.popBackStack()
                        }, modifier = Modifier.size(36.dp)) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                } else {
                    plugin?.let { plugin ->
                        plugin.project?.let { pluginProject ->
                            project?.let { project ->
                                MineUI(plugin, pluginProject, project)
                            }
                        }
                    }
                }
            }
        }

        // UI hot reload implementation
        LaunchedEffect(key1 = appViewModel.state) {
            appViewModel.state.collect {
                when (it) {
                    AppUIState.Default -> {}
                    AppUIState.Loading -> {
                        isLoading = true
                    }

                    is AppUIState.Done -> {
                        plugin = null
                        project = null
                        val plugins = it.plugins
                        for (currentPlugin in plugins) {
                            if (currentPlugin.isSupported && currentPlugin.configuration.enabled() && currentPlugin.packageName == packageName) {
                                plugin = currentPlugin
                                project =
                                    it.projects.find { currentProject -> currentProject.path == path }
                                break
                            }
                        }

                        isLoading = false
                    }
                }
            }
        }

        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event === Lifecycle.Event.ON_RESUME) {
                    if (path.isNotEmpty()) {
                        val rootFile = File(path)
                        val configurationFile = File(path, ".LeafIDE")
                        val workspaceFile = File(configurationFile, "workspace.xml")
                        val result = ProjectManager.parseWorkspace(rootFile, workspaceFile)
                        if (result == null) {
                            appViewModel.intent.trySend(AppUIIntent.Refresh)
                        }
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MineUI(plugin: Plugin, pluginProject: PluginProject, project: Project) {
    val viewModel: EditorViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    var currentPath by rememberSaveable {
        mutableStateOf(project.path)
    }
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    LeafApp(
        title = project.name,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        isLarge = false,
        navigationIcon = {
            IconButton(onClick = {
                showBottomSheet = !showBottomSheet
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Undo,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Redo,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AnimatedVisibility(visible = viewModel.fileTabItems.isNotEmpty()) {
                    FileTabs(
                        items = viewModel.fileTabItems,
                        selectedIndex = viewModel.selectedFileTabIndex,
                        onSelected = { selectedIndex ->
                            val target = viewModel.fileTabItems[selectedIndex]
                            if (viewModel.editingFile?.absolutePath != target.file.absolutePath) {
                                viewModel.intent.trySend(EditorUIIntent.EditingFile(target.file))
                                viewModel.selectedFileTabIndex =
                                    viewModel.selectedFileTabIndex.copy(selectedIndex)
                                viewModel.editingFile = target.file
                            }
                        },
                        onCloseCurrent = { closedIndex ->
                            viewModel.intent.trySend(EditorUIIntent.CloseFile(viewModel.fileTabItems[closedIndex].file))
                        },
                        onCloseOthers = { currentIndex ->
                            viewModel.intent.trySend(EditorUIIntent.CloseOthers(viewModel.fileTabItems[currentIndex].file))
                        },
                        onCloseAll = {
                            viewModel.intent.trySend(EditorUIIntent.CloseAll)
                        }
                    )
                }
                if (viewModel.loading) {
                    LoadingDialog()
                }
                AnimatedContent(
                    targetState = viewModel.editingFile != null,
                    label = "AnimatedContentEditor"
                ) { showEditor ->
                    if (showEditor) {
                        MainEditor(
                            viewModel.content,
                            contentChange = { _, _ ->
                                viewModel.intent.trySend(EditorUIIntent.SaveFile(viewModel.editingFile))
                            }
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row {
                                    Text(
                                        text = stringResource(id = R.string.click_button_to_open),
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "文件列表",
                                        fontSize = 14.sp,
                                        textDecoration = TextDecoration.Underline,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.clickable(
                                            remember {
                                                MutableInteractionSource()
                                            },
                                            indication = null
                                        ) {
                                            showBottomSheet = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false },
                        tonalElevation = 8.dp,
                        containerColor = MaterialTheme.colorScheme.background,
                        dragHandle = {
                            BottomSheetDefaults.DragHandle(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val scrollState = rememberLazyListState()
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                            ) {
                                val (breadcrumb, more) = createRefs()
                                Breadcrumb(
                                    modifier = Modifier.constrainAs(breadcrumb) {
                                        linkTo(
                                            parent.start,
                                            more.start,
                                            endMargin = 10.dp,
                                            bias = 0f
                                        )
                                        width = Dimension.fillToConstraints
                                    },
                                    items = viewModel.breadcrumbItems,
                                    selectedIndex = viewModel.selectedBreadcrumbIndex,
                                    state = scrollState,
                                    onItemClick = { index ->
                                        val currentIndex = viewModel.selectedBreadcrumbIndex

                                        if (index <= currentIndex) {
                                            var workIndex = viewModel.breadcrumbItems.lastIndex
                                            while (workIndex > index) {
                                                viewModel.breadcrumbItems.removeAt(workIndex)
                                                --workIndex
                                            }
                                        }

                                        val item = viewModel.breadcrumbItems[index]
                                        viewModel.selectedBreadcrumbIndex = index
                                        currentPath = item.file.absolutePath
                                        viewModel.intent.trySend(EditorUIIntent.Refresh(currentPath))
                                    }
                                )
                                var expanded by rememberSaveable {
                                    mutableStateOf(false)
                                }
                                IconButton(onClick = {
                                    expanded = true
                                }, modifier = Modifier.constrainAs(more) {
                                    linkTo(parent.start, parent.end, bias = 1f)
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = null
                                    )
                                    OptionDropdownPopup(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        update = {
                                            viewModel.intent.trySend(
                                                EditorUIIntent.Refresh(
                                                    currentPath
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                            FileList(
                                items = viewModel.children,
                                onClick = { child ->
                                    if (child.isDirectory) {
                                        currentPath = child.absolutePath
                                        viewModel.breadcrumbItems.add(BreadcrumbItem(child))
                                        viewModel.selectedBreadcrumbIndex =
                                            viewModel.breadcrumbItems.lastIndex
                                        viewModel.intent.trySend(EditorUIIntent.Refresh(currentPath))
                                        coroutineScope.launch {
                                            scrollState.scrollToItem(viewModel.breadcrumbItems.lastIndex)
                                        }
                                    } else {
                                        viewModel.intent.trySend(
                                            EditorUIIntent.OpenFile(
                                                child
                                            )
                                        )
                                    }
                                },
                                fileIcon = { file ->
                                    pluginProject.getFileIcon(file) ?: IconManager.getFileIcon(file)
                                }
                            )
                        }
                    }
                }

            }
            BackHandler(enabled = showBottomSheet) {
                showBottomSheet = false
            }
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event === Lifecycle.Event.ON_RESUME) {
                if (viewModel.breadcrumbItems.isEmpty()) {
                    viewModel.breadcrumbItems.add(BreadcrumbItem(File(currentPath)))
                }
                var currentFile = File(currentPath)
                while (!currentFile.exists()) {
                    if (currentFile.absolutePath == project.path) {
                        break
                    }
                    viewModel.breadcrumbItems.removeLastOrNull()
                    viewModel.selectedBreadcrumbIndex = viewModel.breadcrumbItems.lastIndex
                    currentFile = currentFile.parentFile ?: break
                }
                if (currentPath != currentFile.absolutePath) {
                    currentPath = currentFile.absolutePath
                }
                if (viewModel.editingFile?.exists() == false) {
                    viewModel.intent.trySend(EditorUIIntent.EditingFile(null))
                }
                viewModel.intent.trySend(EditorUIIntent.Refresh(currentPath))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun OptionDropdownPopup(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    update: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxWidth = 240.dp)
    ) {
        var optionTitleId by rememberSaveable {
            mutableIntStateOf(R.string.options)
        }
        Text(
            text = stringResource(id = optionTitleId),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        val dataStoreManager = DataStoreManager(AppContext.context.SettingsDataStore)
        val displayConfigurationDirRequest = PreferenceRequest(
            key = DisplayConfigurationDirKey,
            defaultValue = false
        )
        var displayConfigurationDir by rememberSaveable {
            mutableStateOf(runBlocking {
                dataStoreManager.getPreference(
                    displayConfigurationDirRequest
                )
            })
        }
        var popupUi by rememberSaveable {
            mutableStateOf("default")
        }
        val coroutineScope = rememberCoroutineScope()
        AnimatedContent(
            targetState = popupUi,
            label = "AnimatedDisplayExpanded"
        ) { ui ->
            Column {
                when (ui) {
                    "default" -> {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.create_file))
                            },
                            onClick = {
                                onDismissRequest()

                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.display))
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                popupUi = "display"
                                optionTitleId = R.string.display
                            }
                        )
                    }

                    "display" -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = if (displayConfigurationDir) R.string.hide_configuration_dir else R.string.display_configuration_dir
                                    )
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    dataStoreManager.editPreference(
                                        displayConfigurationDirRequest.key,
                                        !displayConfigurationDir
                                    )
                                    update()
                                }
                            }
                        )
                    }
                }
            }
        }
        coroutineScope.launch {
            dataStoreManager.getPreferenceFlow(displayConfigurationDirRequest)
                .onEach {
                    displayConfigurationDir = it
                }
                .launchIn(this)
        }
    }
}

@Composable
private fun MainEditor(
    content: Content,
    contentChange: EventReceiver<ContentChangeEvent>? = null
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CodeEditor(
            modifier = Modifier.fillMaxSize(),
            content = content,
            contentChange = contentChange,
            update = {

            }
        )
    }
}