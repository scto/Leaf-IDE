package io.github.caimucheng.leaf.ide.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.application.appViewModel
import io.github.caimucheng.leaf.common.component.Loading
import io.github.caimucheng.leaf.ide.model.Plugin
import io.github.caimucheng.leaf.ide.navhost.LeafIDEDestinations
import io.github.caimucheng.leaf.ide.viewmodel.AppUIState

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayProjectScreen(
    pageNavController: NavHostController
) {
    LeafApp(
        title = stringResource(id = R.string.create_project),
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
        content = { paddings ->
            var isLoading by rememberSaveable {
                mutableStateOf(appViewModel.state.value !is AppUIState.Done)
            }
            var plugins: List<Plugin> by remember {
                mutableStateOf(emptyList())
            }
            Crossfade(
                targetState = isLoading,
                label = "CrossfadeLoading",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                if (it) {
                    Loading()
                } else {
                    NewProjectList(plugins, pageNavController)
                }
            }

            LaunchedEffect(key1 = appViewModel.state) {
                appViewModel.state.collect {
                    when (it) {
                        AppUIState.Default -> {}
                        AppUIState.Loading -> {
                            isLoading = true
                        }

                        is AppUIState.Done -> {
                            plugins = it.plugins
                                .filter { plugin -> plugin.isSupported && plugin.configuration.enabled() }
                            isLoading = false
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewProjectList(plugins: List<Plugin>, pageNavController: NavHostController) {
    if (plugins.isEmpty()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (column) = createRefs()
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .constrainAs(column) {
                        centerTo(parent)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_supported_plugin_project),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.download_from_leaf_flow),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
        ) {
            items(plugins.size) {
                val plugin = plugins[it]
                val pluginProject = plugin.project!!
                val resources = pluginProject.getResources()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(25.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    onClick = {
                        pageNavController.navigate(
                            "${LeafIDEDestinations.CREATE_PROJECT_PAGE}/${plugin.packageName}"
                        )
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier.padding(top = 10.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Icon(
                                painter = BitmapPainter(
                                    ImageBitmap.imageResource(
                                        resources,
                                        pluginProject.getDisplayedPictureResId()
                                    )
                                ),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                        Text(
                            text = resources.getString(pluginProject.getDisplayedTitleId()),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}