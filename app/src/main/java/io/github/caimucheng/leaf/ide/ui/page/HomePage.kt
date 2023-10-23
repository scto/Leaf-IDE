package io.github.caimucheng.leaf.ide.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import io.github.caimucheng.leaf.common.component.Loading
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.application.appViewModel
import io.github.caimucheng.leaf.ide.model.Project
import io.github.caimucheng.leaf.ide.navhost.LeafIDEDestinations
import io.github.caimucheng.leaf.ide.viewmodel.AppUIIntent
import io.github.caimucheng.leaf.ide.viewmodel.AppUIState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
                ProjectList(pageNavController, projects)
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
                pageNavController.navigate(LeafIDEDestinations.DISPLAY_PROJECT_PAGE)
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

@SuppressLint("ReturnFromAwaitPointerEventScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProjectList(pageNavController: NavController, projects: List<Project>) {
    if (projects.isEmpty()) {
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
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(projects.size) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val project = projects[it]
                        val plugin = project.plugin
                        val pluginProject = plugin.project!!
                        val resources = pluginProject.getResources()
                        val animatedOffset = remember {
                            Animatable(Offset.Zero, Offset.VectorConverter)
                        }
                        var expanded by remember {
                            mutableStateOf(false)
                        }


                        Card(
                            Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .combinedClickable(
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    },
                                    indication = null,
                                    onLongClick = {
                                        expanded = true
                                    },
                                    onClick = {
                                        pageNavController.navigate("${LeafIDEDestinations.EDITOR_PAGE}?packageName=${plugin.packageName}&path=${project.path}")
                                    }
                                )
                                .pointerInput(null) {
                                    coroutineScope {
                                        while (true) {
                                            //获取点击位置
                                            val newOffset = awaitPointerEventScope {
                                                awaitFirstDown().position
                                            }
                                            launch {
                                                animatedOffset.animateTo(
                                                    newOffset,
                                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                                )
                                            }
                                        }
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 2.dp
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = project.name,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(
                                        start = 20.dp, end = 20.dp, top = 20.dp
                                    )
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.project_description,
                                        project.description
                                    ),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(
                                        start = 20.dp, end = 20.dp, top = 5.dp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
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
                        Box(modifier = Modifier.offset {
                            IntOffset(
                                animatedOffset.value.x.roundToInt(),
                                animatedOffset.value.y.roundToInt()
                            )
                        }) {
                            ProjectOptionDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                project = project
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectOptionDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    project: Project
) {
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showRenameDialog by rememberSaveable {
        mutableStateOf(false)
    }
    if (showDeleteDialog) {
        AlertDialog(
            title = {
                Text(text = stringResource(id = R.string.delete_project))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_project_confirm, project.name))
            },
            onDismissRequest = {
                showDeleteDialog = false
            },
            confirmButton = {
                ConstraintLayout(Modifier.fillMaxWidth()) {
                    val (cancel, delete) = createRefs()
                    TextButton(
                        onClick = { showDeleteDialog = false },
                        modifier = Modifier.constrainAs(cancel) {
                            linkTo(parent.start, parent.end, bias = 0f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            appViewModel.intent.trySend(AppUIIntent.DeleteProject(project))
                        },
                        modifier = Modifier.constrainAs(delete) {
                            linkTo(cancel.end, parent.end, bias = 1f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                }
            }
        )
    }
    if (showRenameDialog) {
        var name by rememberSaveable {
            mutableStateOf(project.name)
        }
        var nameError by rememberSaveable {
            mutableStateOf("")
        }
        AlertDialog(
            title = {
                Text(text = stringResource(id = R.string.rename_project))
            },
            text = {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = ""
                    },
                    label = {
                        Text(text = stringResource(id = R.string.project_name))
                    },
                    singleLine = true,
                    isError = nameError.isNotEmpty(),
                    supportingText = {
                        if (nameError.isNotEmpty()) {
                            Text(text = nameError)
                        }
                    },
                )
            },
            onDismissRequest = {

            },
            confirmButton = {
                ConstraintLayout(Modifier.fillMaxWidth()) {
                    val (cancel, projectName) = createRefs()
                    val projectNameCannotBeEmpty =
                        stringResource(id = R.string.project_name_cannot_be_empty)
                    val invalidProjectName = stringResource(id = R.string.invalid_project_name)
                    TextButton(
                        onClick = { showRenameDialog = false },
                        modifier = Modifier.constrainAs(cancel) {
                            linkTo(parent.start, parent.end, bias = 0f)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = {
                        if (name.isEmpty()) {
                            nameError = projectNameCannotBeEmpty
                        }

                        if ('/' in name) {
                            nameError = invalidProjectName
                        }

                        if (nameError.isEmpty()) {
                            showRenameDialog = false
                            onDismissRequest()
                            appViewModel.intent.trySend(AppUIIntent.RenameProject(project, name))
                        }
                    }, modifier = Modifier.constrainAs(projectName) {
                        linkTo(cancel.end, parent.end, bias = 1f)
                    }) {
                        Text(text = stringResource(id = R.string.rename))
                    }
                }
            }
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxWidth = 240.dp)
    ) {
        Text(
            text = project.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.rename))
            },
            onClick = {
                showRenameDialog = true
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.delete))
            },
            onClick = {
                showDeleteDialog = true
            }
        )
    }
}