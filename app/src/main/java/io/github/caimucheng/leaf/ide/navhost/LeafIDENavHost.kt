package io.github.caimucheng.leaf.ide.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import io.github.caimucheng.leaf.common.component.AnimatedNavHost
import io.github.caimucheng.leaf.ide.ui.screen.CreateProjectScreen
import io.github.caimucheng.leaf.ide.ui.screen.DisplayProjectScreen
import io.github.caimucheng.leaf.ide.ui.screen.MainScreen
import io.github.caimucheng.leaf.ide.ui.screen.SettingsGeneralScreen

object LeafIDEDestinations {
    const val MAIN_PAGE = "/main"
    const val DISPLAY_PROJECT_PAGE = "/display_project"
    const val CREATE_PROJECT_PAGE = "/display_project/create_project"
    const val SETTINGS_GENERAL_PAGE = "/settings/general"
}

@Composable
fun LeafIDENavHost(pageNavController: NavHostController) {
    AnimatedNavHost(pageNavController, LeafIDEDestinations.MAIN_PAGE) {
        composable(LeafIDEDestinations.MAIN_PAGE) {
            MainScreen(pageNavController)
        }
        composable(LeafIDEDestinations.DISPLAY_PROJECT_PAGE) {
            DisplayProjectScreen(pageNavController)
        }
        composable(
            route = "${LeafIDEDestinations.CREATE_PROJECT_PAGE}/{packageName}",
            arguments = listOf(
                navArgument("packageName") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "rally://${LeafIDEDestinations.CREATE_PROJECT_PAGE}/{packageName}"
            })
        ) {
            val packageName = it.arguments?.getString("packageName") ?: ""
            CreateProjectScreen(pageNavController, packageName)
        }
        composable(LeafIDEDestinations.SETTINGS_GENERAL_PAGE) {
            SettingsGeneralScreen(pageNavController)
        }
    }
}