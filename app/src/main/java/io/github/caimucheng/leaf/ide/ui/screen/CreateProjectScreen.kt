package io.github.caimucheng.leaf.ide.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.caimucheng.leaf.common.component.Loading
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.application.appViewModel
import io.github.caimucheng.leaf.ide.model.Plugin
import io.github.caimucheng.leaf.ide.navhost.LeafIDEDestinations
import io.github.caimucheng.leaf.ide.ui.util.enabled
import io.github.caimucheng.leaf.ide.viewmodel.AppUIState

@Composable
fun CreateProjectScreen(pageNavController: NavController, packageName: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        var isLoading by rememberSaveable {
            mutableStateOf(appViewModel.state.value !is AppUIState.Done)
        }
        var plugin: Plugin? by remember {
            mutableStateOf(null)
        }

        Crossfade(
            targetState = isLoading,
            label = "CrossfadeLoadingCreateProject",
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (it) {
                Loading()
            } else {
                if (plugin == null || plugin?.project == null) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(id = R.string.unable_to_load_create_project_ui))
                        Spacer(modifier = Modifier.height(15.dp))
                        FilledIconButton(onClick = {
                            pageNavController.popBackStack()
                        }, modifier = Modifier.size(36.dp)) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                } else {
                    plugin?.project?.CreateProjectScreen(back = {
                        pageNavController.popBackStack()
                    }, backHome = {
                        pageNavController.popBackStack(
                            route = LeafIDEDestinations.MAIN_PAGE,
                            inclusive = false
                        )
                    })
                }
            }
        }

        // UI hot reload implementation
        LaunchedEffect(key1 = appViewModel.state) {
            appViewModel.state.collect {
                when (it) {
                    AppUIState.Default -> {}
                    AppUIState.Loading -> {
                        isLoading = true
                    }

                    is AppUIState.Done -> {
                        plugin = null
                        val plugins = it.plugins
                        for (currentPlugin in plugins) {
                            if (currentPlugin.isSupported && currentPlugin.configuration.enabled() && currentPlugin.packageName == packageName) {
                                plugin = currentPlugin
                                break
                            }
                        }
                        isLoading = false
                    }
                }
            }
        }
    }
}