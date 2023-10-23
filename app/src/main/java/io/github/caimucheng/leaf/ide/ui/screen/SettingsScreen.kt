package io.github.caimucheng.leaf.ide.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.component.NoImplementation
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.Preference
import io.github.caimucheng.leaf.common.model.PreferenceRequest
import io.github.caimucheng.leaf.common.ui.preferences.PreferenceScreen
import io.github.caimucheng.leaf.common.util.AppBrightnessKey
import io.github.caimucheng.leaf.common.util.AutoDarkLightThemeKey
import io.github.caimucheng.leaf.common.util.EditorColorSchemeKey
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
                    GeneralUI()
                }
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun GeneralUI() {
    val dataStoreManager = DataStoreManager(LocalContext.current.SettingsDataStore)
    val isSupportedMaterialYou = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val isSupportedDarkMode = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val materialYouRequest = PreferenceRequest(
        key = MaterialYouEnabledKey,
        defaultValue = isSupportedMaterialYou
    )
    val autoThemeRequest = PreferenceRequest(
        key = AutoDarkLightThemeKey,
        defaultValue = true
    )
    val appBrightnessRequest = PreferenceRequest(
        key = AppBrightnessKey,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsEditorScreen(pageNavController: NavController) {
    LeafApp(
        title = stringResource(id = R.string.editor_configuration),
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
                    EditorUI()
                }
            }
        }
    )
}

@Stable
private data class ColorSchemeItem(
    val titleId: Int,
    val type: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorUI() {
    val dataStoreManager = DataStoreManager(LocalContext.current.SettingsDataStore)
    val coroutineScope = rememberCoroutineScope()
    val editorColorSchemeRequest = PreferenceRequest(
        key = EditorColorSchemeKey,
        defaultValue = "dynamic"
    )
    val colorSchemeItemList = remember {
        listOf(
            ColorSchemeItem(
                titleId = R.string.dynamic_color_matching,
                type = "dynamic"
            )
        )
    }
    var currentType by remember {
        mutableStateOf(dataStoreManager.getPreferenceBlocking(editorColorSchemeRequest))
    }
    var showColorSchemeDialog by rememberSaveable {
        mutableStateOf(false)
    }
    PreferenceScreen(
        items = listOf(
            Preference.PreferenceGroup(
                title = stringResource(id = R.string.theme),
                preferenceItems = listOf(
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(id = R.string.color_scheme),
                        summary = stringResource(id = R.string.color_scheme_summary),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.ColorLens,
                                contentDescription = stringResource(
                                    id = R.string.color_scheme
                                )
                            )
                        },
                        onClick = {
                            showColorSchemeDialog = true
                        }
                    )
                )
            )
        ),
        modifier = Modifier.fillMaxSize(),
        dataStore = LocalContext.current.SettingsDataStore
    )
    if (showColorSchemeDialog) {
        var selectedType by remember {
            mutableStateOf(currentType)
        }
        AlertDialog(
            onDismissRequest = {
                showColorSchemeDialog = false
            },
            title = {
                Text(text = stringResource(id = R.string.color_scheme))
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(colorSchemeItemList.size) { index ->
                        val item = colorSchemeItemList[index]
                        Card(
                            onClick = {
                                selectedType = item.type
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedType == item.type,
                                    enabled = false,
                                    colors = RadioButtonDefaults.colors(
                                        disabledUnselectedColor = MaterialTheme.colorScheme.onSurface.copy(
                                            0.8f
                                        ),
                                        disabledSelectedColor = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(start = 20.dp),
                                    onClick = {}
                                )
                                Text(
                                    text = stringResource(id = item.titleId),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                ConstraintLayout(Modifier.fillMaxWidth()) {
                    val (cancel, apply) = createRefs()
                    TextButton(
                        onClick = { showColorSchemeDialog = false },
                        modifier = Modifier.constrainAs(cancel) {
                            linkTo(parent.start, parent.end, bias = 0f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            showColorSchemeDialog = false
                            if (currentType != selectedType) {
                                currentType = selectedType
                                coroutineScope.launch {
                                    dataStoreManager.editPreference(
                                        editorColorSchemeRequest.key,
                                        currentType
                                    )
                                }
                            }
                        },
                        modifier = Modifier.constrainAs(apply) {
                            linkTo(cancel.end, parent.end, bias = 1f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.apply))
                    }
                }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBuildAndRunScreen(pageNavController: NavController) {
    LeafApp(
        title = stringResource(id = R.string.build_and_run_configuration),
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
                    NoImplementation()
                }
            }
        }
    )
}