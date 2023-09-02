package io.github.caimucheng.leaf.ide.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caimucheng.leaf.ide.manager.PluginManager
import io.github.caimucheng.leaf.plugin.model.Plugin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch


sealed class MainPageUIState {

    object Default : MainPageUIState()

    object Loading : MainPageUIState()

    data class UnLoadingProject(val plugins: List<Plugin>) : MainPageUIState()

    data class UnLoadingPlugin(val plugins: List<Plugin>) : MainPageUIState()

}

sealed class MainPageUIIntent {
    object RefreshProject : MainPageUIIntent()
    object RefreshPlugin : MainPageUIIntent()
}

class MainPageViewModel : ViewModel() {

    val intent = Channel<MainPageUIIntent>(Channel.UNLIMITED)

    private val _state =
        MutableStateFlow<MainPageUIState>(MainPageUIState.Default)

    val state = _state.asStateFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    MainPageUIIntent.RefreshPlugin -> refreshPlugin()
                    MainPageUIIntent.RefreshProject -> refreshProject()
                }
            }
        }

    }

    private fun refreshProject() {
        // Update state
        _state.value = MainPageUIState.Loading
        viewModelScope.launch {
            // Update plugin list first.
            PluginManager.fetchPlugins()

            val plugins = PluginManager.getPlugins()
                .filter { it.configuration.enabled() && it.project != null }
            // Update state
            _state.value = MainPageUIState.UnLoadingProject(plugins)
        }
    }

    private fun refreshPlugin() {
        // Update state
        _state.value = MainPageUIState.Loading
        viewModelScope.launch {
            // Update plugin list first.
            PluginManager.fetchPlugins()

            val plugins = PluginManager.getPlugins()
            // Update state
            _state.value = MainPageUIState.UnLoadingPlugin(plugins)
        }
    }

}