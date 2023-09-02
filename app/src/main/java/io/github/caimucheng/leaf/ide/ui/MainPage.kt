package io.github.caimucheng.leaf.ide.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.component.NoImplementation
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.activity.CREATE_PROJECT_PAGE
import io.github.caimucheng.leaf.ide.viewmodel.MainPageUIIntent
import io.github.caimucheng.leaf.ide.viewmodel.MainPageUIState
import io.github.caimucheng.leaf.ide.viewmodel.MainPageViewModel
import io.github.caimucheng.leaf.plugin.model.Plugin

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

@Composable
fun Home(pageNavController: NavController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (floatingActionButton) = createRefs()
        LazyColumn(modifier = Modifier.fillMaxSize()) {

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
fun Plugin(viewModel: MainPageViewModel = viewModel()) {
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var plugins: List<Plugin> by remember {
        mutableStateOf(emptyList())
    }
    Crossfade(
        targetState = isLoading,
        label = "CrossfadeLoading",
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (it) {
            LoadingPlugin()
        } else {
            PluginList(plugins)
        }
    }

    val state by viewModel.state.collectAsState()
    when (state) {
        MainPageUIState.Default -> {}
        MainPageUIState.Loading -> {
            isLoading = true
        }

        is MainPageUIState.UnLoading -> {
            plugins = (state as MainPageUIState.UnLoading).plugins
            isLoading = false
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event === Lifecycle.Event.ON_START) {
                viewModel.intent.trySend(MainPageUIIntent.Refresh)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PluginList(plugins: List<Plugin>) {
    if (plugins.isEmpty()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
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
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.download_from_leaf_flow_or_install_from_local),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(plugins.size) {
                    val plugin = plugins[it]
                    val pluginConfiguration = plugin.configuration
                    val resources = plugin.main.getResources()
                    val descriptionId = pluginConfiguration.descriptionId()
                    Card(
                        onClick = {

                        }, colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ), shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
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
                }
            }
        }
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