package io.github.caimucheng.leaf.ide.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import io.github.caimucheng.leaf.common.util.uninstallAPP
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.application.appViewModel
import io.github.caimucheng.leaf.common.component.Loading
import io.github.caimucheng.leaf.ide.model.Plugin
import io.github.caimucheng.leaf.ide.viewmodel.AppUIState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PluginPage() {
    var isLoading by rememberSaveable {
        mutableStateOf(appViewModel.state.value !is AppUIState.Done)
    }
    var plugins: List<Plugin> by remember {
        mutableStateOf(emptyList())
    }
    Crossfade(
        targetState = isLoading,
        label = "CrossfadeLoadingPlugin",
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (it) {
            Loading()
        } else {
            PluginList(plugins)
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
                    plugins = (appViewModel.state.value as AppUIState.Done).plugins
                    isLoading = false
                }
            }
        }
    }
}

@SuppressLint("ReturnFromAwaitPointerEventScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PluginList(plugins: List<Plugin>) {
    if (plugins.isEmpty()) {
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
                    text = stringResource(id = R.string.no_plugin),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.download_from_leaf_flow_or_install_from_local),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(plugins.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val plugin = plugins[it]
                        val pluginConfiguration = plugin.configuration
                        val resources = plugin.main.getResources()
                        val descriptionId = pluginConfiguration.descriptionId()
                        val animatedOffset = remember {
                            Animatable(Offset.Zero, Offset.VectorConverter)
                        }
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ), shape = RoundedCornerShape(0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(onLongClick = {
                                    expanded = true
                                }, onClick = {})
                                .pointerInput(Unit) {
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
                                }
                        ) {
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                                    .alpha(if (plugin.configuration.enabled() && plugin.isSupported) 1f else 0.6f)
                            ) {
                                val (icon, content) = createRefs()
                                Icon(
                                    bitmap = plugin.icon.toBitmap().asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .constrainAs(icon) {
                                            centerVerticallyTo(parent)
                                        },
                                    tint = Color.Unspecified
                                )
                                ConstraintLayout(Modifier.constrainAs(content) {
                                    linkTo(icon.end, parent.end, startMargin = 20.dp)
                                    centerVerticallyTo(parent)
                                    width = Dimension.fillToConstraints
                                }) {
                                    val (name, description, expand, unsupported) = createRefs()
                                    Text(
                                        text = plugin.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.constrainAs(name) {
                                            start.linkTo(parent.start)
                                            top.linkTo(parent.top)
                                        }
                                    )
                                    var showExpandText by remember {
                                        mutableStateOf(false)
                                    }
                                    var maxLines by remember {
                                        mutableIntStateOf(2)
                                    }
                                    if (descriptionId != 0) {
                                        Text(
                                            text = resources.getString(descriptionId),
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                            overflow = TextOverflow.Ellipsis,
                                            onTextLayout = { textLayoutResult ->
                                                if (textLayoutResult.hasVisualOverflow) {
                                                    showExpandText = true
                                                }
                                            },
                                            maxLines = maxLines,
                                            modifier = Modifier
                                                .animateContentSize()
                                                .constrainAs(description) {
                                                    start.linkTo(parent.start)
                                                    top.linkTo(name.bottom, margin = 10.dp)
                                                }
                                        )
                                        if (showExpandText) {
                                            Text(
                                                text = if (maxLines == 2)
                                                    stringResource(id = R.string.expand)
                                                else
                                                    stringResource(id = R.string.collapse),
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                                modifier = Modifier
                                                    .clickable {
                                                        maxLines = if (maxLines == 2) {
                                                            Int.MAX_VALUE
                                                        } else {
                                                            2
                                                        }
                                                    }
                                                    .constrainAs(expand) {
                                                        start.linkTo(parent.start)
                                                        top.linkTo(description.bottom, 10.dp)
                                                    }
                                            )
                                            if (!plugin.isSupported) {
                                                Text(
                                                    text = stringResource(id = R.string.unsupported),
                                                    fontSize = 16.sp,
                                                    color = MaterialTheme.colorScheme.error.copy(
                                                        alpha = 0.8f
                                                    ),
                                                    modifier = Modifier.constrainAs(unsupported) {
                                                        top.linkTo(
                                                            description.bottom,
                                                            margin = 10.dp
                                                        )
                                                        linkTo(
                                                            expand.end,
                                                            parent.end,
                                                            endMargin = 5.dp,
                                                            bias = 1f
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Box(modifier = Modifier.offset {
                            IntOffset(
                                animatedOffset.value.x.roundToInt(),
                                animatedOffset.value.y.roundToInt()
                            )
                        }) {
                            PluginDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                plugin = plugin
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PluginDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    plugin: Plugin
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxWidth = 240.dp)
    ) {
        Text(
            text = plugin.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        val context = LocalContext.current
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.uninstall))
            },
            onClick = {
                onDismissRequest()
                context.uninstallAPP(plugin.packageName)
            }
        )
    }
}