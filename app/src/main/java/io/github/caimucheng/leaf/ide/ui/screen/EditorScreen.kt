package io.github.caimucheng.leaf.ide.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.github.caimucheng.leaf.common.component.Breadcrumb
import io.github.caimucheng.leaf.common.component.CodeEditor
import io.github.caimucheng.leaf.common.component.CodeEditorController
import io.github.caimucheng.leaf.common.component.FileList
import io.github.caimucheng.leaf.common.component.FileTabs
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.component.Loading
import io.github.caimucheng.leaf.common.component.LoadingDialog
import io.github.caimucheng.leaf.common.component.SymbolTabLayout
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.BreadcrumbItem
import io.github.caimucheng.leaf.common.model.PreferenceRequest
import io.github.caimucheng.leaf.common.scheme.DynamicEditorColorScheme
import io.github.caimucheng.leaf.common.util.DisplayConfigurationDirKey
import io.github.caimucheng.leaf.common.util.EditorColorSchemeKey
import io.github.caimucheng.leaf.common.util.SettingsDataStore
import io.github.caimucheng.leaf.common.util.invert
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.application.AppContext
import io.github.caimucheng.leaf.ide.application.appViewModel
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
import io.github.rosemoe.sora.event.SelectionChangeEvent
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.EditorSearcher
import io.github.rosemoe.sora.widget.subscribeEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

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
    var optionsDropdownMenuExpanded by remember {
        mutableStateOf(false)
    }

    var codeEditorController: CodeEditorController? by remember {
        mutableStateOf(null)
    }
    var showSearchPanel by rememberSaveable {
        mutableStateOf(false)
    }
    var pattern by rememberSaveable {
        mutableStateOf("")
    }
    var replacement by rememberSaveable {
        mutableStateOf("")
    }
    var isRegexMode by rememberSaveable {
        mutableStateOf(false)
    }
    var isWholeWordMode by rememberSaveable {
        mutableStateOf(false)
    }
    var isCaseSensitiveMode by rememberSaveable {
        mutableStateOf(false)
    }

    val snackbarHostState = remember { SnackbarHostState() }
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
                    contentDescription = null
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState
            )
        },
        actions = {
            IconButton(
                onClick = {
                    codeEditorController?.undo()
                },
                enabled = viewModel.editingFile != null
            ) {
                Icon(
                    imageVector = Icons.Filled.Undo,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                    codeEditorController?.redo()
                },
                enabled = viewModel.editingFile != null
            ) {
                Icon(
                    imageVector = Icons.Filled.Redo,
                    contentDescription = null
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                optionsDropdownMenuExpanded = true
            }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null
                )
                if (optionsDropdownMenuExpanded) {
                    var showColorPickerDialog by remember {
                        mutableStateOf(false)
                    }
                    OptionDropdownMenu(
                        projectName = project.name,
                        editingFile = viewModel.editingFile,
                        expanded = optionsDropdownMenuExpanded,
                        onReopenFile = {
                            viewModel.intent.trySend(EditorUIIntent.ReopenFile(viewModel.editingFile))
                        },
                        onStatisticsFile = {
                            viewModel.intent.trySend(EditorUIIntent.StatisticsFile(viewModel.editingFile))
                        },
                        onStatisticsProject = {
                            viewModel.intent.trySend(EditorUIIntent.StatisticsProject(project.path))
                        },
                        onSearch = {
                            showSearchPanel = true
                        },
                        onColorPick = {
                            showColorPickerDialog = true
                        }
                    ) { optionsDropdownMenuExpanded = false }
                    if (viewModel.statisticsFileContent.isNotEmpty() && !viewModel.loading) {
                        StatisticsFileDialog(
                            content = viewModel.content,
                            statisticsFileContent = viewModel.statisticsFileContent,
                            onDismissRequest = {
                                viewModel.statisticsFileContent = emptyMap()
                            }
                        )
                    }
                    if (viewModel.statisticsProjectContent.isNotEmpty() && !viewModel.loading) {
                        StatisticsProjectDialog(
                            project = project,
                            statisticsProjectContent = viewModel.statisticsProjectContent,
                            onDismissRequest = {
                                viewModel.statisticsProjectContent = emptyMap()
                            }
                        )
                    }
                    if (showColorPickerDialog) {
                        ColorPickerDialog(
                            onDismissRequest = {
                                showColorPickerDialog = false
                            }
                        )
                    }
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                BackHandler(enabled = showSearchPanel) {
                    codeEditorController?.apply {
                        searcher().stopSearch()
                        postInvalidate()
                    }
                    showSearchPanel = false
                }
                AnimatedVisibility(visible = viewModel.fileTabItems.isNotEmpty()) {
                    FileTabs(
                        items = viewModel.fileTabItems,
                        selectedIndex = viewModel.selectedFileTabIndex,
                        onSelected = { selectedIndex ->
                            val target = viewModel.fileTabItems[selectedIndex]
                            if (viewModel.editingFile?.absolutePath != target.file.absolutePath) {
                                viewModel.intent.trySend(
                                    EditorUIIntent.EditingFile(
                                        target.file, target.cursorPosition
                                    )
                                )
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
                        ConstraintLayout(Modifier.fillMaxSize()) {
                            val (codeEditor, layout) = createRefs()
                            val dataStoreManager =
                                DataStoreManager(LocalContext.current.SettingsDataStore)
                            val colorScheme = MaterialTheme.colorScheme
                            val handleColor = LocalTextSelectionColors.current.handleColor
                            val selectionBackgroundColor =
                                LocalTextSelectionColors.current.backgroundColor
                            val editorColorSchemeRequest = PreferenceRequest(
                                key = EditorColorSchemeKey,
                                defaultValue = "dynamic"
                            )
                            val currentType = remember {
                                dataStoreManager.getPreferenceBlocking(editorColorSchemeRequest)
                            }
                            CodeEditor(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(codeEditor) {
                                        linkTo(parent.start, parent.end)
                                        linkTo(parent.top, layout.top)
                                        height = Dimension.fillToConstraints
                                    },
                                content = viewModel.content,
                                colorScheme = remember(currentType) {
                                    when (currentType) {
                                        "dynamic" -> DynamicEditorColorScheme(
                                            colorScheme = colorScheme,
                                            handleColor = handleColor,
                                            selectionBackgroundColor = selectionBackgroundColor
                                        )

                                        else -> throw RuntimeException("Stub!")
                                    }
                                },
                                cursorPosition = viewModel.cursorPosition
                            ) { editor ->
                                if (editor.horizontalScrollbarThumbDrawable != null) {
                                    editor.horizontalScrollbarThumbDrawable = null
                                }
                                if (editor.verticalScrollbarThumbDrawable != null) {
                                    editor.verticalScrollbarThumbDrawable = null
                                }
                                editor.subscribeEvent<ContentChangeEvent> { _, _ ->
                                    viewModel.intent.trySend(EditorUIIntent.SaveFile(viewModel.editingFile))
                                }
                                editor.subscribeEvent<SelectionChangeEvent> { event, _ ->
                                    viewModel.intent.trySend(
                                        EditorUIIntent.SaveCursorState(
                                            event.left
                                        )
                                    )
                                }
                                codeEditorController = CodeEditorController(editor)
                            }
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .animateContentSize()
                                    .constrainAs(layout) {
                                        linkTo(parent.start, parent.end)
                                        linkTo(parent.top, parent.bottom, bias = 1f)
                                    }
                            ) {
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp),
                                    color = DividerDefaults.color.copy(alpha = 0.4f)
                                )
                                SymbolTabLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .background(MaterialTheme.colorScheme.background)
                                )
                                if (showSearchPanel) {
                                    var isReplacingMode by rememberSaveable {
                                        mutableStateOf(false)
                                    }
                                    val regexSyntaxException =
                                        stringResource(id = R.string.regex_syntax_exception)
                                    SideEffect {
                                        if (pattern.isNotEmpty()) {
                                            try {
                                                codeEditorController?.searcher()
                                                    ?.search(
                                                        pattern,
                                                        EditorSearcher.SearchOptions(
                                                            when {
                                                                !isRegexMode && !isWholeWordMode -> {
                                                                    EditorSearcher.SearchOptions.TYPE_NORMAL
                                                                }

                                                                isRegexMode -> {
                                                                    EditorSearcher.SearchOptions.TYPE_REGULAR_EXPRESSION
                                                                }

                                                                else -> {
                                                                    EditorSearcher.SearchOptions.TYPE_WHOLE_WORD
                                                                }
                                                            },
                                                            !isCaseSensitiveMode,
                                                        )
                                                    )
                                            } catch (_: PatternSyntaxException) {
                                            }
                                        }
                                    }
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp),
                                        color = DividerDefaults.color.copy(alpha = 0.4f)
                                    )
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .background(MaterialTheme.colorScheme.background),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.search),
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        )
                                        val interactionSource = remember {
                                            MutableInteractionSource()
                                        }
                                        Column(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(end = 10.dp)
                                        ) {
                                            var isFocused by remember {
                                                mutableStateOf(false)
                                            }
                                            val dividerHeight by animateDpAsState(
                                                targetValue = if (isFocused) 2.dp else 1.dp,
                                                label = "animateHeight"
                                            )
                                            val dividerColor by animateColorAsState(
                                                targetValue = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                                label = "animateColor"
                                            )
                                            BasicTextField(
                                                value = pattern,
                                                onValueChange = { text ->
                                                    pattern = text
                                                    if (pattern.isNotEmpty()) {
                                                        try {
                                                            codeEditorController?.searcher()
                                                                ?.search(
                                                                    pattern,
                                                                    EditorSearcher.SearchOptions(
                                                                        when {
                                                                            !isRegexMode && !isWholeWordMode -> {
                                                                                EditorSearcher.SearchOptions.TYPE_NORMAL
                                                                            }

                                                                            isRegexMode -> {
                                                                                EditorSearcher.SearchOptions.TYPE_REGULAR_EXPRESSION
                                                                            }

                                                                            else -> {
                                                                                EditorSearcher.SearchOptions.TYPE_WHOLE_WORD
                                                                            }
                                                                        },
                                                                        !isCaseSensitiveMode
                                                                    )
                                                                )
                                                        } catch (_: PatternSyntaxException) {
                                                        }
                                                    } else {
                                                        codeEditorController?.apply {
                                                            searcher().stopSearch()
                                                            postInvalidate()
                                                        }
                                                    }
                                                },
                                                textStyle = LocalTextStyle.current.copy(
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                ),
                                                interactionSource = interactionSource,
                                                singleLine = true,
                                                cursorBrush = SolidColor(LocalTextSelectionColors.current.handleColor),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .onFocusChanged { state ->
                                                        isFocused = state.isFocused
                                                    }
                                                    .focusRequester(FocusRequester.Default)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Divider(
                                                modifier = Modifier.fillMaxWidth(),
                                                thickness = dividerHeight,
                                                color = dividerColor
                                            )
                                        }
                                    }
                                    if (isReplacingMode) {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(40.dp)
                                                .background(MaterialTheme.colorScheme.background),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.replace),
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(horizontal = 10.dp)
                                            )
                                            val interactionSource = remember {
                                                MutableInteractionSource()
                                            }
                                            Column(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(end = 10.dp)
                                            ) {
                                                var isFocused by remember {
                                                    mutableStateOf(false)
                                                }
                                                val dividerHeight by animateDpAsState(
                                                    targetValue = if (isFocused) 2.dp else 1.dp,
                                                    label = "animateHeight"
                                                )
                                                val dividerColor by animateColorAsState(
                                                    targetValue = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                                    label = "animateColor"
                                                )
                                                BasicTextField(
                                                    value = replacement,
                                                    onValueChange = { text ->
                                                        replacement = text
                                                    },
                                                    textStyle = LocalTextStyle.current.copy(
                                                        fontSize = 14.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    ),
                                                    interactionSource = interactionSource,
                                                    singleLine = true,
                                                    cursorBrush = SolidColor(
                                                        LocalTextSelectionColors.current.handleColor
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .onFocusChanged { state ->
                                                            isFocused = state.isFocused
                                                        }
                                                        .focusRequester(FocusRequester.Default)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Divider(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    thickness = dividerHeight,
                                                    color = dividerColor
                                                )
                                            }
                                        }
                                    }
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp),
                                        color = DividerDefaults.color.copy(alpha = 0.4f)
                                    )
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .background(MaterialTheme.colorScheme.background)
                                    ) {
                                        val textNotFound =
                                            stringResource(id = R.string.text_not_found)
                                        val totalReplacement =
                                            stringResource(id = R.string.total_replacement)
                                        TextButton(
                                            onClick = {
                                                codeEditorController?.searcher()?.apply {
                                                    if (hasQuery() && pattern.isNotEmpty()) {
                                                        try {
                                                            if (isRegexMode) {
                                                                Pattern.compile(pattern)
                                                            }
                                                            if (matchedPositionCount == 0) {
                                                                coroutineScope.launch {
                                                                    snackbarHostState.currentSnackbarData?.dismiss()
                                                                    snackbarHostState.showSnackbar(
                                                                        textNotFound,
                                                                        withDismissAction = true
                                                                    )
                                                                }
                                                                return@apply
                                                            }
                                                            gotoPrevious()
                                                        } catch (_: IllegalStateException) {
                                                        } catch (e: PatternSyntaxException) {
                                                            coroutineScope.launch {
                                                                snackbarHostState.currentSnackbarData?.dismiss()
                                                                snackbarHostState.showSnackbar(
                                                                    message = regexSyntaxException.format(
                                                                        e.message
                                                                    ),
                                                                    withDismissAction = true,
                                                                    duration = SnackbarDuration.Long
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            shape = RoundedCornerShape(0.dp),
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = MaterialTheme.colorScheme.onSurface
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.previous),
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        TextButton(
                                            onClick = {
                                                codeEditorController?.searcher()?.apply {
                                                    if (hasQuery() && pattern.isNotEmpty()) {
                                                        try {
                                                            if (isRegexMode) {
                                                                Pattern.compile(pattern)
                                                            }
                                                            if (matchedPositionCount == 0) {
                                                                coroutineScope.launch {
                                                                    snackbarHostState.currentSnackbarData?.dismiss()
                                                                    snackbarHostState.showSnackbar(
                                                                        textNotFound,
                                                                        withDismissAction = true
                                                                    )
                                                                }
                                                                return@apply
                                                            }
                                                            gotoNext()
                                                        } catch (_: IllegalStateException) {
                                                        } catch (e: PatternSyntaxException) {
                                                            coroutineScope.launch {
                                                                snackbarHostState.currentSnackbarData?.dismiss()
                                                                snackbarHostState.showSnackbar(
                                                                    message = regexSyntaxException.format(
                                                                        e.message
                                                                    ),
                                                                    withDismissAction = true,
                                                                    duration = SnackbarDuration.Long
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            shape = RoundedCornerShape(0.dp),
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = MaterialTheme.colorScheme.onSurface
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.next),
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        TextButton(
                                            onClick = {
                                                if (!isReplacingMode) {
                                                    isReplacingMode = true
                                                } else {
                                                    if (pattern.isEmpty()) {
                                                        return@TextButton
                                                    }
                                                    codeEditorController?.apply {
                                                        val searcher = searcher()
                                                        try {
                                                            if (isRegexMode) {
                                                                Pattern.compile(pattern)
                                                            }
                                                            val count =
                                                                searcher.matchedPositionCount
                                                            if (count == 0) {
                                                                coroutineScope.launch {
                                                                    snackbarHostState.currentSnackbarData?.dismiss()
                                                                    snackbarHostState.showSnackbar(
                                                                        textNotFound,
                                                                        withDismissAction = true
                                                                    )
                                                                }
                                                                return@apply
                                                            }
                                                            searcher.replaceThis(replacement)
                                                        } catch (_: IllegalStateException) {
                                                        } catch (e: PatternSyntaxException) {
                                                            coroutineScope.launch {
                                                                snackbarHostState.currentSnackbarData?.dismiss()
                                                                snackbarHostState.showSnackbar(
                                                                    message = regexSyntaxException.format(
                                                                        e.message
                                                                    ),
                                                                    withDismissAction = true,
                                                                    duration = SnackbarDuration.Long
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            shape = RoundedCornerShape(0.dp),
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = MaterialTheme.colorScheme.onSurface
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.replace),
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        TextButton(
                                            onClick = {
                                                if (pattern.isEmpty()) {
                                                    return@TextButton
                                                }
                                                codeEditorController?.apply {
                                                    val searcher = searcher()
                                                    val count = searcher.matchedPositionCount
                                                    if (count == 0) {
                                                        coroutineScope.launch {
                                                            snackbarHostState.currentSnackbarData?.dismiss()
                                                            snackbarHostState.showSnackbar(
                                                                textNotFound,
                                                                withDismissAction = true
                                                            )
                                                        }
                                                        return@apply
                                                    }
                                                    try {
                                                        if (isRegexMode) {
                                                            Pattern.compile(pattern)
                                                        }
                                                        searcher.replaceAll(replacement) {
                                                            coroutineScope.launch {
                                                                snackbarHostState.currentSnackbarData?.dismiss()
                                                                snackbarHostState.showSnackbar(
                                                                    totalReplacement.format(count),
                                                                    withDismissAction = true
                                                                )
                                                            }
                                                        }
                                                    } catch (_: IllegalStateException) {
                                                    } catch (e: PatternSyntaxException) {
                                                        coroutineScope.launch {
                                                            snackbarHostState.currentSnackbarData?.dismiss()
                                                            snackbarHostState.showSnackbar(
                                                                message = regexSyntaxException.format(
                                                                    e.message
                                                                ),
                                                                withDismissAction = true,
                                                                duration = SnackbarDuration.Long
                                                            )
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            shape = RoundedCornerShape(0.dp),
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = MaterialTheme.colorScheme.onSurface
                                            ),
                                            enabled = isReplacingMode
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.all),
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        var showMoreOptions by remember {
                                            mutableStateOf(false)
                                        }
                                        Box(modifier = Modifier.size(40.dp)) {
                                            TextButton(
                                                onClick = {
                                                    showMoreOptions = true
                                                },
                                                modifier = Modifier.fillMaxSize(),
                                                shape = RoundedCornerShape(0.dp),
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = MaterialTheme.colorScheme.onSurface
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.MoreVert,
                                                    contentDescription = null
                                                )
                                            }

                                            DropdownMenu(
                                                expanded = showMoreOptions,
                                                onDismissRequest = {
                                                    showMoreOptions = false
                                                },
                                                modifier = Modifier
                                                    .sizeIn(maxWidth = 240.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.search_options),
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
                                                        Text(
                                                            text = stringResource(id = R.string.regex)
                                                        )
                                                    },
                                                    trailingIcon = {
                                                        AnimatedContent(
                                                            targetState = isRegexMode,
                                                            label = "animateRegexContent"
                                                        ) { value ->
                                                            if (value) {
                                                                Icon(
                                                                    imageVector = Icons.Filled.CheckBox,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(20.dp),
                                                                    tint = MaterialTheme.colorScheme.primary
                                                                )
                                                            } else {
                                                                Icon(
                                                                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(20.dp),
                                                                    tint = MaterialTheme.colorScheme.onSurface
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onClick = {
                                                        isRegexMode = !isRegexMode
                                                        isWholeWordMode = false
                                                        codeEditorController?.apply {
                                                            val searcher = searcher()
                                                            if (searcher.hasQuery() && pattern.isNotEmpty()) {
                                                                try {
                                                                    codeEditorController?.searcher()
                                                                        ?.search(
                                                                            pattern,
                                                                            EditorSearcher.SearchOptions(
                                                                                when {
                                                                                    !isRegexMode && !isWholeWordMode -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_NORMAL
                                                                                    }

                                                                                    isRegexMode -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_REGULAR_EXPRESSION
                                                                                    }

                                                                                    else -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_WHOLE_WORD
                                                                                    }
                                                                                },
                                                                                !isCaseSensitiveMode
                                                                            )
                                                                        )
                                                                } catch (_: PatternSyntaxException) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = stringResource(id = R.string.whole_word)
                                                        )
                                                    },
                                                    trailingIcon = {
                                                        AnimatedContent(
                                                            targetState = isWholeWordMode,
                                                            label = "animateWholeWordContent"
                                                        ) { value ->
                                                            if (value) {
                                                                Icon(
                                                                    imageVector = Icons.Filled.CheckBox,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(20.dp),
                                                                    tint = MaterialTheme.colorScheme.primary
                                                                )
                                                            } else {
                                                                Icon(
                                                                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(20.dp),
                                                                    tint = MaterialTheme.colorScheme.onSurface
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onClick = {
                                                        isWholeWordMode = !isWholeWordMode
                                                        isRegexMode = false
                                                        codeEditorController?.apply {
                                                            val searcher = searcher()
                                                            if (searcher.hasQuery() && pattern.isNotEmpty()) {
                                                                try {
                                                                    codeEditorController?.searcher()
                                                                        ?.search(
                                                                            pattern,
                                                                            EditorSearcher.SearchOptions(
                                                                                when {
                                                                                    !isRegexMode && !isWholeWordMode -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_NORMAL
                                                                                    }

                                                                                    isRegexMode -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_REGULAR_EXPRESSION
                                                                                    }

                                                                                    else -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_WHOLE_WORD
                                                                                    }
                                                                                },
                                                                                !isCaseSensitiveMode
                                                                            )
                                                                        )
                                                                } catch (_: PatternSyntaxException) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = stringResource(id = R.string.case_sensitive)
                                                        )
                                                    },
                                                    trailingIcon = {
                                                        AnimatedContent(
                                                            targetState = isCaseSensitiveMode,
                                                            label = "animateCaseSensitiveContent"
                                                        ) { value ->
                                                            if (value) {
                                                                Icon(
                                                                    imageVector = Icons.Filled.CheckBox,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(20.dp),
                                                                    tint = MaterialTheme.colorScheme.primary
                                                                )
                                                            } else {
                                                                Icon(
                                                                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(20.dp),
                                                                    tint = MaterialTheme.colorScheme.onSurface
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onClick = {
                                                        isCaseSensitiveMode = !isCaseSensitiveMode
                                                        codeEditorController?.apply {
                                                            val searcher = searcher()
                                                            if (searcher.hasQuery() && pattern.isNotEmpty()) {
                                                                try {
                                                                    codeEditorController?.searcher()
                                                                        ?.search(
                                                                            pattern,
                                                                            EditorSearcher.SearchOptions(
                                                                                when {
                                                                                    !isRegexMode && !isWholeWordMode -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_NORMAL
                                                                                    }

                                                                                    isRegexMode -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_REGULAR_EXPRESSION
                                                                                    }

                                                                                    else -> {
                                                                                        EditorSearcher.SearchOptions.TYPE_WHOLE_WORD
                                                                                    }
                                                                                },
                                                                                !isCaseSensitiveMode
                                                                            )
                                                                        )
                                                                } catch (_: PatternSyntaxException) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
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
                                        text = stringResource(id = R.string.click_navigation_button_to_open),
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = stringResource(id = R.string.file_list),
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
                                Spacer(modifier = Modifier.height(10.dp))
                                Row {
                                    Text(
                                        text = stringResource(id = R.string.click_menu_button_to_open),
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = stringResource(id = R.string.options_menu),
                                        fontSize = 14.sp,
                                        textDecoration = TextDecoration.Underline,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.clickable(
                                            remember {
                                                MutableInteractionSource()
                                            },
                                            indication = null
                                        ) {
                                            optionsDropdownMenuExpanded = true
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
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                                        currentPath = currentPath,
                                        expanded = expanded,
                                        viewModel = viewModel,
                                        onDismissRequest = { expanded = false }
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
                                onRename = { file, newName ->
                                    viewModel.intent.trySend(
                                        EditorUIIntent.RenameFile(
                                            currentPath,
                                            file,
                                            newName
                                        )
                                    )
                                },
                                onDelete = { file ->
                                    viewModel.intent.trySend(
                                        EditorUIIntent.DeleteFile(
                                            currentPath,
                                            file
                                        )
                                    )
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

@Composable
private fun ColorPickerDialog(
    onDismissRequest: () -> Unit
) {
    val controller = rememberColorPickerController()
    var hexCode: String by remember {
        mutableStateOf("#FFFFFFFF")
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            Column {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope ->
                        hexCode = "#${colorEnvelope.hexCode}"
                    }
                )
                Spacer(modifier = Modifier.height(30.dp))
                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    borderSize = 0.dp,
                    borderColor = Color.Transparent,
                    borderRadius = 16.dp,
                    controller = controller,
                )
                Spacer(modifier = Modifier.height(30.dp))
                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    borderSize = 0.dp,
                    borderColor = Color.Transparent,
                    borderRadius = 16.dp,
                    controller = controller,
                )
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(controller.selectedColor.value),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hexCode,
                            color = controller.selectedColor.value.invert
                        )
                    }
                }
            }
        },
        confirmButton = {
            val clipboardManager = LocalClipboardManager.current
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (cancel, copy) = createRefs()
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.constrainAs(cancel) {
                        linkTo(parent.start, parent.end, bias = 0f)
                    }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                TextButton(
                    onClick = {
                        onDismissRequest()
                        clipboardManager.setText(AnnotatedString(hexCode))
                    },
                    modifier = Modifier.constrainAs(copy) {
                        linkTo(cancel.end, parent.end, bias = 1f)
                    }
                ) {
                    Text(text = stringResource(id = R.string.copy))
                }
            }
        }
    )
}

@Composable
private fun StatisticsFileDialog(
    content: Content,
    statisticsFileContent: Map<String, String>,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.statistical_results))
        },
        text = {
            Text(text = buildString {
                appendLine(
                    stringResource(
                        id = R.string.file_name_statistics,
                        statisticsFileContent["fileName"] ?: "unknown"
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.disk_usage,
                        statisticsFileContent["diskUsage"] ?: "unknown"
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.disk_usage_not_formatted,
                        statisticsFileContent["diskUsageNotFormatted"] ?: "unknown"
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.total_line_count,
                        content.lineCount
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.total_length,
                        content.length
                    )
                )
            })
        },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

@Composable
private fun StatisticsProjectDialog(
    project: Project,
    statisticsProjectContent: Map<String, String>,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.statistical_results))
        },
        text = {
            Text(text = buildString {
                appendLine(
                    stringResource(
                        id = R.string.project_name_statistics,
                        project.name
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.file_total_count,
                        statisticsProjectContent["fileTotalCount"] ?: "unknown"
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.file_count,
                        statisticsProjectContent["fileCount"] ?: "unknown"
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.folder_count,
                        statisticsProjectContent["folderCount"] ?: "unknown"
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.disk_usage,
                        statisticsProjectContent["diskUsage"] ?: "unknown"
                    )
                )
                appendLine(
                    stringResource(
                        id = R.string.disk_usage_not_formatted,
                        statisticsProjectContent["diskUsageNotFormatted"] ?: "unknown"
                    )
                )
            })
        },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

@Composable
private fun OptionDropdownMenu(
    projectName: String,
    editingFile: File?,
    expanded: Boolean,
    onReopenFile: () -> Unit,
    onStatisticsFile: () -> Unit,
    onStatisticsProject: () -> Unit,
    onSearch: () -> Unit,
    onColorPick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var optionTitle by remember {
        mutableStateOf(projectName)
    }
    var popupUi by remember {
        mutableStateOf("default")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxWidth = 240.dp)
    ) {
        val stringFile = stringResource(id = R.string.file)
        val stringProject = stringResource(id = R.string.project)
        val stringCode = stringResource(id = R.string.code)
        val stringTool = stringResource(id = R.string.tool)
        Text(
            text = optionTitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        AnimatedContent(
            targetState = popupUi,
            label = "AnimatedOptionAExpanded"
        ) { ui ->
            Column {
                when (ui) {
                    "default" -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringFile,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                popupUi = "file"
                                optionTitle = stringFile
                            },
                            enabled = editingFile != null
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringProject)
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                popupUi = "project"
                                optionTitle = stringProject
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringCode)
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                popupUi = "code"
                                optionTitle = stringCode
                            },
                            enabled = editingFile != null
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringTool)
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                popupUi = "tool"
                                optionTitle = stringTool
                            }
                        )
                    }

                    "file" -> {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.reopen))
                            },
                            onClick = {
                                onReopenFile()
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.statistics_file))
                            },
                            onClick = {
                                onStatisticsFile()
                            }
                        )
                    }

                    "project" -> {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.statistics_project))
                            },
                            onClick = {
                                onStatisticsProject()
                            }
                        )
                    }

                    "code" -> {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.search))
                            },
                            onClick = {
                                onSearch()
                                onDismissRequest()
                            }
                        )
                    }

                    "tool" -> {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.color_picker))
                            },
                            onClick = {
                                onColorPick()
                            }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun OptionDropdownPopup(
    currentPath: String,
    expanded: Boolean,
    viewModel: EditorViewModel,
    onDismissRequest: () -> Unit
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
        var createFileDialog by rememberSaveable {
            mutableStateOf(false)
        }
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
                                createFileDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.display))
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = null
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
                                    viewModel.intent.trySend(EditorUIIntent.Refresh(currentPath))
                                }
                            }
                        )
                    }
                }
            }
        }
        if (createFileDialog) {
            var name by rememberSaveable {
                mutableStateOf("")
            }
            var nameError by rememberSaveable {
                mutableStateOf("")
            }
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(text = stringResource(id = R.string.create_file))
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
                confirmButton = {
                    ConstraintLayout(Modifier.fillMaxWidth()) {
                        val (cancel, directory, file) = createRefs()
                        val fileNameCannotBeEmpty =
                            stringResource(id = R.string.file_name_cannot_be_empty)
                        val invalidFileName = stringResource(id = R.string.invalid_file_name)
                        TextButton(
                            onClick = { createFileDialog = false },
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
                                    createFileDialog = false
                                    onDismissRequest()
                                    viewModel.intent.trySend(
                                        EditorUIIntent.CreateFile(
                                            currentPath,
                                            name,
                                            isDirectory = true
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.constrainAs(directory) {
                                linkTo(cancel.end, file.start, endMargin = 8.dp, bias = 1f)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.directory))
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
                                    createFileDialog = false
                                    onDismissRequest()
                                    viewModel.intent.trySend(
                                        EditorUIIntent.CreateFile(
                                            currentPath,
                                            name,
                                            isDirectory = false
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.constrainAs(file) {
                                linkTo(cancel.end, parent.end, bias = 1f)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.file))
                        }
                    }
                }
            )
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