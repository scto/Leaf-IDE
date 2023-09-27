package io.github.caimucheng.leaf.ide.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.caimucheng.leaf.common.component.Breadcrumb
import io.github.caimucheng.leaf.common.component.FileList
import io.github.caimucheng.leaf.common.component.FileTab
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.model.BreadcrumbItem
import io.github.caimucheng.leaf.ide.R
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
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MineUI(plugin: Plugin, pluginProject: PluginProject, project: Project) {
    val viewModel: EditorViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var currentPath by rememberSaveable {
        mutableStateOf(project.path)
    }
    LeafApp(
        title = project.name,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        isLarge = false,
        navigationIcon = {
            IconButton(onClick = {
                if (drawerState.isOpen) {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                } else {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
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
            ModalNavigationDrawer(
                drawerContent = {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 60.dp),
                        shape = RoundedCornerShape(0.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
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
                                    selectedIndex = viewModel.selectedIndex,
                                    state = scrollState,
                                    onItemClick = { index ->
                                        val currentIndex = viewModel.selectedIndex

                                        if (index <= currentIndex) {
                                            var workIndex = viewModel.breadcrumbItems.lastIndex
                                            while (workIndex > index) {
                                                viewModel.breadcrumbItems.removeAt(workIndex)
                                                --workIndex
                                            }
                                        }

                                        val item = viewModel.breadcrumbItems[index]
                                        viewModel.selectedIndex = index
                                        currentPath = item.file.absolutePath
                                        viewModel.intent.trySend(EditorUIIntent.Refresh(currentPath))
                                    }
                                )
                                IconButton(onClick = {

                                }, modifier = Modifier.constrainAs(more) {
                                    linkTo(parent.start, parent.end, bias = 1f)
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = null
                                    )
                                }
                            }
                            FileList(
                                items = viewModel.children,
                                onClick = { child ->
                                    if (child.isDirectory) {
                                        currentPath = child.absolutePath
                                        viewModel.breadcrumbItems.add(BreadcrumbItem(child))
                                        viewModel.selectedIndex =
                                            viewModel.breadcrumbItems.lastIndex
                                        viewModel.intent.trySend(EditorUIIntent.Refresh(currentPath))
                                        coroutineScope.launch {
                                            scrollState.scrollToItem(viewModel.breadcrumbItems.lastIndex)
                                        }
                                    } else {

                                    }
                                },
                                fileIcon = { file ->
                                    pluginProject.getFileIcon(file) ?: IconManager.getFileIcon(file)
                                }
                            )
                        }
                    }
                },
                drawerState = drawerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    FileTab(modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp))
                    Spacer(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray))
                }
            }
            BackHandler(enabled = drawerState.isOpen) {
                coroutineScope.launch {
                    drawerState.close()
                }
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
                    viewModel.selectedIndex--
                    currentFile = currentFile.parentFile ?: break
                }
                if (currentPath != currentFile.absolutePath) {
                    currentPath = currentFile.absolutePath
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