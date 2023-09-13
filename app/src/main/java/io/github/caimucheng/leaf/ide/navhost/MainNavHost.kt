package io.github.caimucheng.leaf.ide.navhost

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.HOME
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.LEAF_FLOW
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.PLUGIN
import io.github.caimucheng.leaf.ide.navhost.MainNavHostDestinations.SETTINGS
import io.github.caimucheng.leaf.ide.ui.page.HomePage
import io.github.caimucheng.leaf.ide.ui.page.LeafFlowPage
import io.github.caimucheng.leaf.ide.ui.page.PluginPage
import io.github.caimucheng.leaf.ide.ui.page.SettingsPage

object MainNavHostDestinations {
    const val HOME = "home"
    const val PLUGIN = "plugin"
    const val LEAF_FLOW = "leaf_flow"
    const val SETTINGS = "settings"
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    pageNavController: NavController,
) {
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