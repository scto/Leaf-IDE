package io.github.caimucheng.leaf.ide.ui

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.ide.R
import io.github.caimucheng.leaf.ide.viewmodel.CreateProjectUIIntent
import io.github.caimucheng.leaf.ide.viewmodel.CreateProjectUIState
import io.github.caimucheng.leaf.ide.viewmodel.CreateProjectViewModel
import io.github.caimucheng.leaf.plugin.model.Plugin

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectPage(
    pageNavController: NavHostController,
    viewModel: CreateProjectViewModel = viewModel()
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
                mutableStateOf(true)
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
                    NewProjectList(plugins)
                }
            }

            val state by viewModel.state.collectAsState()
            when (state) {
                CreateProjectUIState.Default -> {}
                CreateProjectUIState.Loading -> {
                    isLoading = true
                }
                is CreateProjectUIState.UnLoading -> {
                    plugins = (state as CreateProjectUIState.UnLoading).plugins
                    isLoading = false
                }
            }

            val lifecycle = LocalLifecycleOwner.current.lifecycle
            DisposableEffect(key1 = lifecycle) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event === ON_START) {
                        viewModel.intent.trySend(CreateProjectUIIntent.Refresh)
                    }
                }
                lifecycle.addObserver(observer)
                onDispose {
                    lifecycle.removeObserver(observer)
                }
            }
        }
    )
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp),
            strokeWidth = 5.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewProjectList(plugins: List<Plugin>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .focusable(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
        items(plugins.size) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                onClick = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.nodejs),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = "NodeJS 工程",
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