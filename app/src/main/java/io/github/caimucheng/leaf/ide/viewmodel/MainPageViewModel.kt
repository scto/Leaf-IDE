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
    data class UnLoading(val plugins: List<Plugin>) : MainPageUIState()
}

sealed class MainPageUIIntent {
    object Refresh : MainPageUIIntent()

}

class MainPageViewModel : ViewModel() {

    val intent = Channel<MainPageUIIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<MainPageUIState>(MainPageUIState.Default)

    val state = _state.asStateFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                if (it is MainPageUIIntent.Refresh) {
                    refresh()
                }
            }
        }
    }

    private fun refresh() {
        // Update state
        _state.value = MainPageUIState.Loading
        viewModelScope.launch {
            // Update plugin list first.
            PluginManager.fetchPlugins()

            val plugins = PluginManager.getPlugins()
            // Update state
            _state.value = MainPageUIState.UnLoading(plugins)
        }
    }

}