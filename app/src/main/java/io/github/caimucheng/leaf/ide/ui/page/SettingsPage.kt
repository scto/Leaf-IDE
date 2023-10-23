package io.github.caimucheng.leaf.ide.ui.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.github.caimucheng.leaf.common.model.Preference
import io.github.caimucheng.leaf.common.ui.preferences.PreferenceScreen
import io.github.caimucheng.leaf.common.util.SettingsDataStore
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.navhost.LeafIDEDestinations

@Composable
fun SettingsPage(pageNavController: NavController) {
    PreferenceScreen(
        items = listOf(
            Preference.PreferenceGroup(
                title = stringResource(id = R.string.configuration),
                preferenceItems = listOf(
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(id = R.string.general),
                        summary = stringResource(id = R.string.general_summary),
                        singleLineTitle = true,
                        onClick = {
                            pageNavController.navigate(LeafIDEDestinations.SETTINGS_GENERAL_PAGE)
                        }
                    ),
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(id = R.string.editor),
                        summary = stringResource(id = R.string.editor_summary),
                        singleLineTitle = true,
                        onClick = {
                            pageNavController.navigate(LeafIDEDestinations.SETTINGS_EDITOR_PAGE)
                        }
                    ),
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(id = R.string.build_and_run),
                        summary = stringResource(id = R.string.build_and_run_summary),
                        singleLineTitle = true,
                        onClick = {
                            pageNavController.navigate(LeafIDEDestinations.SETTINGS_BUILD_AND_RUN_PAGE)
                        }
                    )
                )
            )
        ),
        modifier = Modifier.fillMaxSize(),
        dataStore = LocalContext.current.SettingsDataStore
    )
}