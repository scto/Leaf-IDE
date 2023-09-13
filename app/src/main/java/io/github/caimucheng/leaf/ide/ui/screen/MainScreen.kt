package io.github.caimucheng.leaf.ide.ui.screen

import androidx.annotation.StringRes
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
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.icon.Needle
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.navhost.MainNavHost
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.HOME
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.LEAF_FLOW
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.PLUGIN
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.SETTINGS

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
        MainNavHost(
            navController = navController,
            pageNavController = pageNavController
        )
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