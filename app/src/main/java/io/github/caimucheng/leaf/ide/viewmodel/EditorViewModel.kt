package io.github.caimucheng.leaf.ide.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caimucheng.leaf.common.model.BreadcrumbItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.io.File

sealed class EditorUIIntent {

    data class Refresh(val path: String) : EditorUIIntent()

}

class EditorViewModel : ViewModel() {

    val breadcrumbItems: MutableList<BreadcrumbItem> = mutableStateListOf()

    var selectedIndex: Int by mutableIntStateOf(0)

    val children: MutableList<File> = mutableStateListOf()

    val intent: Channel<EditorUIIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is EditorUIIntent.Refresh -> refresh(it.path)
                }
            }
        }
    }

    private fun refresh(path: String) {
        children.clear()
        viewModelScope.launch {
            val file = File(path)
            val files = file.listFiles() ?: emptyArray()
            files.sortWith { first, second ->
                if (first.isDirectory && second.isFile) {
                    return@sortWith -1
                }
                if (first.isFile && second.isDirectory) {
                    return@sortWith 1
                }
                first.name.compareTo(second.name)
            }
            children.addAll(files)
        }
    }

}