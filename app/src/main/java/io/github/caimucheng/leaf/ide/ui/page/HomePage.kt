package io.github.caimucheng.leaf.ide.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.component.Loading
import io.github.caimucheng.leaf.ide.navhost.LeafIDEDestinations
import io.github.caimucheng.leaf.plugin.application.appViewModel
import io.github.caimucheng.leaf.plugin.manager.PluginManager
import io.github.caimucheng.leaf.plugin.model.Project
import io.github.caimucheng.leaf.plugin.viewmodel.AppUIIntent
import io.github.caimucheng.leaf.plugin.viewmodel.AppUIState

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomePage(pageNavController: NavController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (floatingActionButton, crossFade) = createRefs()

        var isLoading by rememberSaveable {
            mutableStateOf(appViewModel.state.value !is AppUIState.Done)
        }
        var projects: List<Project> by remember {
            mutableStateOf(emptyList())
        }
        Crossfade(
            targetState = isLoading,
            label = "CrossfadeLoadingHome",
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(crossFade) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        ) {
            if (it) {
                Loading()
            } else {
                ProjectList(projects)
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
                        projects = it.projects
                            .filter { project -> project.plugin.configuration.enabled() }
                        isLoading = false
                    }
                }
            }
        }

        if (appViewModel.state.value === AppUIState.Default) {
            appViewModel.intent.trySend(AppUIIntent.Refresh)
        }

        FloatingActionButton(
            onClick = {
                pageNavController.navigate(LeafIDEDestinations.CREATE_PROJECT_PAGE)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(48.dp),
            modifier = Modifier.constrainAs(floatingActionButton) {
                end.linkTo(parent.end, 15.dp)
                bottom.linkTo(parent.bottom, 15.dp)
            }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.create_project)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectList(projects: List<Project>) {
    if (projects.isNotEmpty()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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
                    text = stringResource(id = R.string.no_project_or_plugins_for_projects),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.click_to_create_project),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    } else {
        if (PluginManager.getPlugins().firstOrNull() == null) {
            return
        }
        val testLists = listOf(
            Project(
                "NodeJS Project",
                PluginManager.getPlugins().first(),
                emptyMap()
            )
        )
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(testLists.size) {
                    val project = testLists[it]
                    val plugin = project.plugin
                    val pluginProject = plugin.project!!
                    val resources = pluginProject.getResources()

                    Card(
                        onClick = {},
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                text = project.name,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(
                                    start = 20.dp, end = 20.dp, top = 20.dp
                                )
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.plugin_support,
                                    plugin.packageName
                                ),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(
                                    start = 20.dp, end = 20.dp, top = 5.dp
                                ),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, top = 15.dp)
                                    .height(1.dp),
                                color = DividerDefaults.color.copy(alpha = 0.4f)
                            )
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 15.dp,
                                        bottom = 15.dp
                                    ),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(
                                    bitmap = resources.getDrawable(
                                        pluginProject.getDisplayedProjectLogoResId(),
                                        LocalContext.current.theme
                                    ).toBitmap().asImageBitmap(),
                                    tint = Color.Unspecified,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = resources.getString(pluginProject.getDisplayedProjectTitleId()),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}