package io.github.caimucheng.leaf.ide.viewmodel

import android.app.Application
import io.github.caimucheng.leaf.ide.manager.PluginManager
import io.github.caimucheng.leaf.ide.manager.ProjectManager
import io.github.caimucheng.leaf.ide.model.Plugin
import io.github.caimucheng.leaf.ide.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

sealed class AppUIState {

    data object Default : AppUIState()

    data object Loading : AppUIState()

    data class Done(
        val projects: List<Project>,
        val plugins: List<Plugin>
    ) : AppUIState()

}

sealed class AppUIIntent {

    data object Refresh : AppUIIntent()

    data class DeleteProject(val project: Project) : AppUIIntent()

}

class AppViewModel(private val application: Application) {

    private val _coroutineScope = CoroutineScope(Dispatchers.Default)

    val intent = Channel<AppUIIntent>(Channel.UNLIMITED)

    private val _state =
        MutableStateFlow<AppUIState>(AppUIState.Default)

    val state = _state.asStateFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        _coroutineScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    AppUIIntent.Refresh -> refresh()
                    is AppUIIntent.DeleteProject -> deleteProject(it.project)
                }
            }
        }

    }

    private fun refresh() {
        // Update state
        _state.value = AppUIState.Loading
        _coroutineScope.launch {

            // Update plugin list first.
            PluginManager.fetchPlugins(application)

            // Update project list
            ProjectManager.fetchProjects()

            val projects = ProjectManager.getProjects()

            // Update state
            _state.value = AppUIState.Done(projects, PluginManager.getPlugins())

        }
    }

    private fun deleteProject(project: Project) {
        _state.value = AppUIState.Loading
        _coroutineScope.launch {
            ProjectManager.deleteProject(project)

            val projects = ProjectManager.getProjects()
            _state.value = AppUIState.Done(projects, PluginManager.getPlugins())

        }
    }

}