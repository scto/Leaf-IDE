package io.github.caimucheng.leaf.ide.ui.screen

import androidx.annotation.StringRes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.icon.Needle
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.ui.page.HomePage
import io.github.caimucheng.leaf.ide.ui.page.LeafFlowPage
import io.github.caimucheng.leaf.ide.ui.page.PluginPage
import io.github.caimucheng.leaf.ide.ui.page.SettingsPage

private const val HOME = "home"
private const val PLUGIN = "plugin"
private const val LEAF_FLOW = "leaf_flow"
private const val SETTINGS = "settings"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(pageNavController: NavController) {
    val navController = rememberNavController()
    LeafApp(
        title = stringResource(id = R.string.app_name),
        content = {
            AppContent(
                paddingValues = it,
                pageNavController = pageNavController,
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController
            )
        }
    )
}

@Composable
private fun AppContent(
    paddingValues: PaddingValues,
    pageNavController: NavController,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = HOME,
                enterTransition = { scaleIn(initialScale = 0.8f) + fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                composable(HOME) {
                    HomePage(pageNavController)
                }
                composable(PLUGIN) {
                    PluginPage()
                }
                composable(LEAF_FLOW) {
                    LeafFlowPage()
                }
                composable(SETTINGS) {
                    SettingsPage(pageNavController)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(navController: NavController) {
    var selectedPage by rememberSaveable { mutableStateOf(HOME) }
    val pages = listOf(
        PageData(
            titleResId = R.string.home,
            icon = Icons.Filled.Home,
            pageDestination = HOME
        ),
        PageData(
            titleResId = R.string.plugin,
            icon = Icons.Filled.Needle,
            pageDestination = PLUGIN
        ),
        PageData(
            titleResId = R.string.leaf_flow,
            icon = Icons.Filled.EnergySavingsLeaf,
            pageDestination = LEAF_FLOW
        ),
        PageData(
            titleResId = R.string.settings,
            icon = Icons.Filled.Settings,
            pageDestination = SETTINGS
        )
    )
    NavigationBar {
        pages.forEach { item ->
            NavigationBarItem(
                selected = selectedPage == item.pageDestination,
                alwaysShowLabel = false,
                onClick = {
                    if (selectedPage != item.pageDestination) {
                        navController.navigate(item.pageDestination) {
                            popUpTo(selectedPage) {
                                inclusive = true
                            }
                        }
                        selectedPage = item.pageDestination
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.titleResId)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.titleResId))
                }
            )
        }
    }
}

private data class PageData(
    @StringRes val titleResId: Int,
    val icon: ImageVector,
    val pageDestination: String
)