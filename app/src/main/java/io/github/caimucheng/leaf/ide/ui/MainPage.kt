package io.github.caimucheng.leaf.ide.ui

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.component.NoImplementation
import io.github.caimucheng.leaf.common.util.uninstallAPP
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.activity.CREATE_PROJECT_PAGE
import io.github.caimucheng.leaf.plugin.application.appViewModel
import io.github.caimucheng.leaf.plugin.model.Plugin
import io.github.caimucheng.leaf.plugin.model.Project
import io.github.caimucheng.leaf.plugin.viewmodel.AppUIIntent
import io.github.caimucheng.leaf.plugin.viewmodel.AppUIState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val HOME = "home"
private const val PLUGIN = "plugin"
private const val LEAF_FLOW = "leaf_flow"
private const val SETTINGS = "settings"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(pageNavController: NavController) {
    var selectedPage by rememberSaveable {
        mutableStateOf(HOME)
    }
    val navController = rememberNavController()
    LeafApp(
        title = stringResource(id = R.string.app_name),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = HOME,
                        enterTransition = {
                            scaleIn(initialScale = 0.8f) + fadeIn()
                        },
                        exitTransition = {
                            fadeOut()
                        }
                    ) {
                        composable(HOME) {
                            Home(pageNavController)
                        }
                        composable(PLUGIN) {
                            Plugin()
                        }
                        composable(LEAF_FLOW) {
                            LeafFlow()
                        }
                        composable(SETTINGS) {
                            Settings()
                        }
                    }
                }
            }
        },
        bottomBar = {
            val home = stringResource(id = R.string.home)
            val plugin = stringResource(id = R.string.plugin)
            val leafFlow = stringResource(id = R.string.leaf_flow)
            val settings = stringResource(id = R.string.settings)
            NavigationBar {
                NavigationBarItem(
                    selected = selectedPage == HOME,
                    alwaysShowLabel = false,
                    onClick = {
                        if (selectedPage != HOME) {
                            navController.navigate(HOME) {
                                popUpTo(selectedPage) {
                                    inclusive = true
                                }
                            }
                            selectedPage = HOME
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = home
                        )
                    },
                    label = {
                        Text(text = home)
                    }
                )
                NavigationBarItem(
                    selected = selectedPage == PLUGIN,
                    alwaysShowLabel = false,
                    onClick = {
                        if (selectedPage != PLUGIN) {
                            navController.navigate(PLUGIN) {
                                popUpTo(selectedPage) {
                                    inclusive = true
                                }
                            }
                            selectedPage = PLUGIN
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.needle),
                            contentDescription = plugin
                        )
                    },
                    label = {
                        Text(text = plugin)
                    }
                )
                NavigationBarItem(
                    selected = selectedPage == LEAF_FLOW,
                    alwaysShowLabel = false,
                    onClick = {
                        if (selectedPage != LEAF_FLOW) {
                            navController.navigate(LEAF_FLOW) {
                                popUpTo(selectedPage) {
                                    inclusive = true
                                }
                            }
                            selectedPage = LEAF_FLOW
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.EnergySavingsLeaf,
                            contentDescription = leafFlow
                        )
                    },
                    label = {
                        Text(text = leafFlow)
                    }
                )
                NavigationBarItem(
                    selected = selectedPage == SETTINGS,
                    alwaysShowLabel = false,
                    onClick = {
                        if (selectedPage != SETTINGS) {
                            navController.navigate(SETTINGS) {
                                popUpTo(selectedPage) {
                                    inclusive = true
                                }
                            }
                            selectedPage = SETTINGS
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = settings
                        )
                    },
                    label = {
                        Text(text = settings)
                    }
                )
            }
        }
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Home(pageNavController: NavController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (floatingActionButton, crossFade) = createRefs()

        var isLoading by rememberSaveable {
            mutableStateOf(appViewModel.state.value !is AppUIState.Done)
        }
        var projects: List<Project> by remember {
            mutableStateOf(emptyList())
        }
        Crossfade(
            targetState = isLoading,
            label = "CrossfadeLoadingHome",
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(crossFade) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        ) {
            if (it) {
                LoadingPlugin()
            } else {
                ProjectList(projects)
            }
        }

        LaunchedEffect(key1 = appViewModel.state) {
            appViewModel.state.collect {
                when (it) {
                    AppUIState.Default -> {}
                    AppUIState.Loading -> {
                        isLoading = true
                    }

                    is AppUIState.Done -> {
                        projects = it.projects
                            .filter { project -> project.plugin.configuration.enabled() }
                        isLoading = false
                    }
                }
            }
        }

        if (appViewModel.state.value === AppUIState.Default) {
            appViewModel.intent.trySend(AppUIIntent.Refresh)
        }

        FloatingActionButton(
            onClick = {
                pageNavController.navigate(CREATE_PROJECT_PAGE)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(48.dp),
            modifier = Modifier.constrainAs(floatingActionButton) {
                end.linkTo(parent.end, 15.dp)
                bottom.linkTo(parent.bottom, 15.dp)
            }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.create_project)
            )
        }
    }
}

@Composable
private fun ProjectList(projects: List<Project>) {
    if (projects.isEmpty()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val (column) = createRefs()
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .constrainAs(column) {
                        centerTo(parent)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_project_or_plugins_for_projects),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.click_to_create_project),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    } else {

    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Plugin() {
    var isLoading by rememberSaveable {
        mutableStateOf(appViewModel.state.value !is AppUIState.Done)
    }
    var plugins: List<Plugin> by remember {
        mutableStateOf(emptyList())
    }
    Crossfade(
        targetState = isLoading,
        label = "CrossfadeLoadingPlugin",
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (it) {
            LoadingPlugin()
        } else {
            PluginList(plugins)
        }
    }

    LaunchedEffect(key1 = appViewModel.state) {
        appViewModel.state.collect {
            when (it) {
                AppUIState.Default -> {}
                AppUIState.Loading -> {
                    isLoading = true
                }

                is AppUIState.Done -> {
                    plugins = (appViewModel.state.value as AppUIState.Done).plugins
                    isLoading = false
                }
            }
        }
    }
}

@SuppressLint("ReturnFromAwaitPointerEventScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PluginList(plugins: List<Plugin>) {
    if (plugins.isEmpty()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val (column) = createRefs()
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .constrainAs(column) {
                        centerTo(parent)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_plugin),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.download_from_leaf_flow_or_install_from_local),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(plugins.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val plugin = plugins[it]
                        val pluginConfiguration = plugin.configuration
                        val resources = plugin.main.getResources()
                        val descriptionId = pluginConfiguration.descriptionId()
                        val animatedOffset = remember {
                            Animatable(Offset.Zero, Offset.VectorConverter)
                        }
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ), shape = RoundedCornerShape(0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(onLongClick = {
                                    expanded = true
                                }, onClick = {})
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
                                }
                        ) {
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                                    .alpha(if (plugin.configuration.enabled()) 1f else 0.6f)
                            ) {
                                val (icon, content) = createRefs()
                                Icon(
                                    bitmap = plugin.icon.toBitmap().asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .constrainAs(icon) {
                                            centerVerticallyTo(parent)
                                        },
                                    tint = Color.Unspecified
                                )
                                Column(Modifier.constrainAs(content) {
                                    linkTo(icon.end, parent.end, startMargin = 20.dp)
                                    centerVerticallyTo(parent)
                                    width = Dimension.fillToConstraints
                                }) {
                                    Text(
                                        text = plugin.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    var showExpandText by remember {
                                        mutableStateOf(false)
                                    }
                                    var maxLines by remember {
                                        mutableIntStateOf(2)
                                    }
                                    if (descriptionId != 0) {
                                        Text(
                                            text = resources.getString(descriptionId),
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                            overflow = TextOverflow.Ellipsis,
                                            onTextLayout = { textLayoutResult ->
                                                if (textLayoutResult.hasVisualOverflow) {
                                                    showExpandText = true
                                                }
                                            },
                                            maxLines = maxLines,
                                            modifier = Modifier.animateContentSize()
                                        )
                                        if (showExpandText) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = if (maxLines == 2)
                                                    stringResource(id = R.string.expand)
                                                else
                                                    stringResource(id = R.string.collapse),
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                                modifier = Modifier.clickable {
                                                    maxLines = if (maxLines == 2) {
                                                        Int.MAX_VALUE
                                                    } else {
                                                        2
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Box(modifier = Modifier.offset {
                            IntOffset(
                                animatedOffset.value.x.roundToInt(),
                                animatedOffset.value.y.roundToInt()
                            )
                        }) {
                            PluginDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                plugin = plugin
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PluginDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    plugin: Plugin
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxWidth = 240.dp)
    ) {
        Text(
            text = plugin.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        val context = LocalContext.current
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.uninstall))
            },
            onClick = {
                onDismissRequest()
                context.uninstallAPP(plugin.packageName)
            }
        )
    }
}

@Composable
private fun LoadingPlugin() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp),
            strokeWidth = 5.dp
        )
    }
}

@Composable
fun LeafFlow() {
    NoImplementation()
}

@Composable
fun Settings() {
    NoImplementation()
}