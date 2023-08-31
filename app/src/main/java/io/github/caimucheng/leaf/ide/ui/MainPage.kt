package io.github.caimucheng.leaf.ide.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.component.NoImplementation
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.activity.CREATE_PROJECT_PAGE

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
fun Plugin() {
    NoImplementation()
}

@Composable
fun LeafFlow() {
    NoImplementation()
}

@Composable
fun Settings() {
    NoImplementation()
}