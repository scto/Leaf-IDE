package io.github.caimucheng.leaf.ide.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.Preference
import io.github.caimucheng.leaf.common.model.PreferenceRequest
import io.github.caimucheng.leaf.common.ui.preferences.PreferenceScreen
import io.github.caimucheng.leaf.common.util.AppBrightness
import io.github.caimucheng.leaf.common.util.AutoDarkLightTheme
import io.github.caimucheng.leaf.common.util.MaterialYouEnabledKey
import io.github.caimucheng.leaf.common.util.SettingsDataStore
import io.github.caimucheng.leaf.ide.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsGeneralScreen(pageNavController: NavHostController) {
    LeafApp(
        title = stringResource(id = R.string.general_configuration),
        navigationIcon = {
            IconButton(onClick = {
                pageNavController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    MineUI()
                }
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun MineUI() {
    val dataStoreManager = DataStoreManager(LocalContext.current.SettingsDataStore)
    val isSupportedMaterialYou = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val isSupportedDarkMode = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val materialYouRequest = PreferenceRequest(
        key = MaterialYouEnabledKey,
        defaultValue = isSupportedMaterialYou
    )
    val autoThemeRequest = PreferenceRequest(
        key = AutoDarkLightTheme,
        defaultValue = true
    )
    val appBrightnessRequest = PreferenceRequest(
        key = AppBrightness,
        defaultValue = false
    )
    var appBrightnessVisible by rememberSaveable {
        mutableStateOf(!runBlocking { dataStoreManager.getPreference(appBrightnessRequest) })
    }

    PreferenceScreen(
        items = listOf(
            Preference.PreferenceGroup(
                title = stringResource(id = R.string.configuration),
                preferenceItems = listOf(
                    Preference.PreferenceItem.SwitchPreference(
                        request = materialYouRequest,
                        title = stringResource(id = R.string.material_you),
                        summary = stringResource(id = if (isSupportedMaterialYou) R.string.material_you_summary_supported else R.string.material_you_summary_unsupported),
                        singleLineTitle = true,
                        enabled = isSupportedMaterialYou,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.ColorLens,
                                contentDescription = stringResource(
                                    id = R.string.material_you
                                )
                            )
                        }
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        request = autoThemeRequest,
                        title = stringResource(id = R.string.auto_light_and_dark_theme),
                        summary = stringResource(id = R.string.auto_light_and_dark_theme_summary),
                        singleLineTitle = true,
                        enabled = isSupportedDarkMode,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Brightness6,
                                contentDescription = stringResource(
                                    id = R.string.auto_light_and_dark_theme
                                )
                            )
                        }
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        request = appBrightnessRequest,
                        title = stringResource(id = R.string.dark_theme),
                        summary = stringResource(id = R.string.dark_theme_summary),
                        singleLineTitle = true,
                        visible = appBrightnessVisible,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Brightness2,
                                contentDescription = stringResource(
                                    id = R.string.dark_theme
                                )
                            )
                        }
                    )
                )
            )
        ),
        modifier = Modifier.fillMaxSize(),
        dataStore = LocalContext.current.SettingsDataStore
    )

    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        dataStoreManager.getPreferenceFlow(autoThemeRequest)
            .onEach {
                appBrightnessVisible = !it
            }.launchIn(this)
    }
}