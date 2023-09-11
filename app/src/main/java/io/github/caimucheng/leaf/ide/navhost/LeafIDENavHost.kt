package io.github.caimucheng.leaf.ide.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import io.github.caimucheng.leaf.common.component.AnimatedNavHost
import io.github.caimucheng.leaf.ide.ui.screen.CreateProjectScreen
import io.github.caimucheng.leaf.ide.ui.screen.MainScreen
import io.github.caimucheng.leaf.ide.ui.screen.SettingsGeneralScreen

object LeafIDEDestinations {
    const val MAIN_PAGE = "/main_page"
    const val CREATE_PROJECT_PAGE = "/create_project_page"
    const val SETTINGS_GENERAL_PAGE = "/settings_general_page"
}

@Composable
fun LeafIDENavHost(pageNavController: NavHostController) {
    AnimatedNavHost(pageNavController, LeafIDEDestinations.MAIN_PAGE) {
        composable(LeafIDEDestinations.MAIN_PAGE) {
            MainScreen(pageNavController)
        }
        composable(LeafIDEDestinations.CREATE_PROJECT_PAGE) {
            CreateProjectScreen(pageNavController)
        }
        composable(LeafIDEDestinations.SETTINGS_GENERAL_PAGE) {
            SettingsGeneralScreen(pageNavController)
        }
    }
}