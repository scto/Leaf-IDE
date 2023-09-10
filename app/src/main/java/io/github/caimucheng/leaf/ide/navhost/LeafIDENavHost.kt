package io.github.caimucheng.leaf.ide.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import io.github.caimucheng.leaf.common.component.AnimatedNavHost
import io.github.caimucheng.leaf.ide.ui.CreateProjectPage
import io.github.caimucheng.leaf.ide.ui.MainPage
import io.github.caimucheng.leaf.ide.ui.SettingsGeneralPage

object Destinations {
    const val MAIN_PAGE = "/main_page"
    const val CREATE_PROJECT_PAGE = "/create_project_page"
    const val SETTINGS_GENERAL_PAGE = "/settings_general_page"
}

@Composable
fun LeafIDENavHost(pageNavController: NavHostController) {
    AnimatedNavHost(pageNavController, Destinations.MAIN_PAGE) {
        composable(Destinations.MAIN_PAGE) {
            MainPage(pageNavController)
        }
        composable(Destinations.CREATE_PROJECT_PAGE) {
            CreateProjectPage(pageNavController)
        }
        composable(Destinations.SETTINGS_GENERAL_PAGE) {
            SettingsGeneralPage(pageNavController)
        }
    }
}