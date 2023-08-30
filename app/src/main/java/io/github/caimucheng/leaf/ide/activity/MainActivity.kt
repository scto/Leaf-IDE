package io.github.caimucheng.leaf.ide.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.ui.theme.LeafIDETheme
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.ui.Home
import io.github.caimucheng.leaf.ide.ui.LeafFlow
import io.github.caimucheng.leaf.ide.ui.Plugin
import io.github.caimucheng.leaf.ide.ui.Settings

private const val HOME = "home"
private const val PLUGIN = "plugin"
private const val LEAF_FLOW = "leaf_flow"
private const val SETTINGS = "settings"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LeafIDETheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Main() {
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
                            Home()
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