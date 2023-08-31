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

sealed class CreateProjectUIState {

    object Default : CreateProjectUIState()

    object Loading : CreateProjectUIState()
    data class UnLoading(val plugins: List<Plugin>) : CreateProjectUIState()
}

sealed class CreateProjectUIIntent {
    object Refresh : CreateProjectUIIntent()

}

class CreateProjectViewModel : ViewModel() {

    val intent = Channel<CreateProjectUIIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CreateProjectUIState>(CreateProjectUIState.Default)

    val state = _state.asStateFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                if (it is CreateProjectUIIntent.Refresh) {
                    refresh()
                }
            }
        }
    }

    private fun refresh() {
        // Update state
        _state.value = CreateProjectUIState.Loading
        viewModelScope.launch {
            // Update plugin list first.
            PluginManager.fetchPlugins()

            val plugins = PluginManager.getPlugins()
                .filter { it.configuration.enabled() && it.project != null }
            // Update state
            _state.value = CreateProjectUIState.UnLoading(plugins)
        }
    }

}