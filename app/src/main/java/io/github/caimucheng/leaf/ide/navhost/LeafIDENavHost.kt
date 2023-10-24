package io.github.caimucheng.leaf.ide.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.caimucheng.leaf.common.component.AnimatedNavHost
import io.github.caimucheng.leaf.ide.ui.screen.CreateProjectScreen
import io.github.caimucheng.leaf.ide.ui.screen.DisplayProjectScreen
import io.github.caimucheng.leaf.ide.ui.screen.EditorScreen
import io.github.caimucheng.leaf.ide.ui.screen.MainScreen
import io.github.caimucheng.leaf.ide.ui.screen.SettingsBuildAndRunScreen
import io.github.caimucheng.leaf.ide.ui.screen.SettingsColorSchemeDebugging
import io.github.caimucheng.leaf.ide.ui.screen.SettingsDeveloperOptionsScreen
import io.github.caimucheng.leaf.ide.ui.screen.SettingsEditorScreen
import io.github.caimucheng.leaf.ide.ui.screen.SettingsGeneralScreen

object LeafIDEDestinations {
    const val MAIN_PAGE = "/main"
    const val EDITOR_PAGE = "/editor"
    const val DISPLAY_PROJECT_PAGE = "/display_project"
    const val CREATE_PROJECT_PAGE = "/display_project/create_project"
    const val SETTINGS_GENERAL_PAGE = "/settings/general"
    const val SETTINGS_EDITOR_PAGE = "/settings/editor"
    const val SETTINGS_BUILD_AND_RUN_PAGE = "/settings/build_and_run"
    const val SETTINGS_DEVELOPER_OPTIONS_PAGE = "/settings/developer_options"
    const val SETTINGS_COLOR_SCHEME_DEBUGGING_PAGE = "/settings/developer_options/color_scheme_debugging"
}

@Composable
fun LeafIDENavHost(pageNavController: NavHostController) {
    AnimatedNavHost(pageNavController, LeafIDEDestinations.MAIN_PAGE) {
        composable(LeafIDEDestinations.MAIN_PAGE) {
            MainScreen(pageNavController)
        }
        composable(
            route = "${LeafIDEDestinations.EDITOR_PAGE}?packageName={packageName}&path={path}",
            arguments = listOf(
                navArgument("packageName") {
                    type = NavType.StringType
                },
                navArgument("path") {
                    type = NavType.StringType
                }
            )
        ) {
            val packageName = it.arguments?.getString("packageName") ?: ""
            val path = it.arguments?.getString("path") ?: ""
            EditorScreen(pageNavController, packageName, path)
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
            )
        ) {
            val packageName = it.arguments?.getString("packageName") ?: ""
            CreateProjectScreen(pageNavController, packageName)
        }
        composable(LeafIDEDestinations.SETTINGS_GENERAL_PAGE) {
            SettingsGeneralScreen(pageNavController)
        }
        composable(LeafIDEDestinations.SETTINGS_EDITOR_PAGE) {
            SettingsEditorScreen(pageNavController)
        }
        composable(LeafIDEDestinations.SETTINGS_BUILD_AND_RUN_PAGE) {
            SettingsBuildAndRunScreen(pageNavController)
        }
        composable(LeafIDEDestinations.SETTINGS_DEVELOPER_OPTIONS_PAGE) {
            SettingsDeveloperOptionsScreen(pageNavController)
        }
        composable(LeafIDEDestinations.SETTINGS_COLOR_SCHEME_DEBUGGING_PAGE) {
            SettingsColorSchemeDebugging(pageNavController)
        }
    }
}