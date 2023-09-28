package io.github.caimucheng.leaf.ide.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caimucheng.leaf.common.model.BreadcrumbItem
import io.github.caimucheng.leaf.common.model.FileTabItem
import io.github.caimucheng.leaf.common.model.Value
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.text.ContentIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

sealed class EditorUIIntent {

    data class Refresh(val path: String) : EditorUIIntent()

    data class OpenFile(val file: File) : EditorUIIntent()

    data class CloseFile(val file: File) : EditorUIIntent()

    data class CloseOthers(val currentFile: File) : EditorUIIntent()

    data object CloseAll : EditorUIIntent()

    data class EditingFile(val file: File?) : EditorUIIntent()

}

class EditorViewModel : ViewModel() {

    val breadcrumbItems: MutableList<BreadcrumbItem> = mutableStateListOf()

    val fileTabItems: MutableList<FileTabItem> = mutableStateListOf()

    var selectedBreadcrumbIndex: Int by mutableIntStateOf(0)

    var selectedFileTabIndex: Value<Int> by mutableStateOf(Value(0))

    val children: MutableList<File> = mutableStateListOf()

    var editingFile: File? by mutableStateOf(null)

    var content: Content by mutableStateOf(Content())

    var loading: Boolean by mutableStateOf(false)

    val intent: Channel<EditorUIIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is EditorUIIntent.Refresh -> refresh(it.path)
                    is EditorUIIntent.OpenFile -> openFile(it.file)
                    is EditorUIIntent.CloseFile -> closeFile(it.file)
                    is EditorUIIntent.CloseOthers -> closeOthers(it.currentFile)
                    is EditorUIIntent.EditingFile -> editingFile(it.file)
                    EditorUIIntent.CloseAll -> closeAll()
                }
            }
        }
    }

    private fun editingFile(file: File?) {
        loading = true
        viewModelScope.launch {
            file?.let {
                content = withContext(Dispatchers.IO) {
                    ContentIO.createFrom(FileInputStream(it), Charset.forName("UTF-8"))
                }
            }
            editingFile = file
            loading = false
        }
    }

    private fun closeAll() {
        viewModelScope.launch {
            fileTabItems.clear()
            selectedFileTabIndex = Value(0)
            editingFile(null)
        }
    }

    private fun closeOthers(currentFile: File) {
        viewModelScope.launch {
            for (fileTabItem in fileTabItems) {
                if (fileTabItem.file.absolutePath == currentFile.absolutePath) {
                    val subtractList = fileTabItems subtract setOf(fileTabItem)
                    fileTabItems.removeAll(subtractList)
                    selectedFileTabIndex = Value(0)
                    break
                }
            }
        }
    }

    private fun closeFile(file: File) {
        viewModelScope.launch {
            for ((index, fileTab) in fileTabItems.withIndex()) {
                if (fileTab.file.absolutePath == file.absolutePath) {
                    selectedFileTabIndex = if (selectedFileTabIndex.value > index) {
                        selectedFileTabIndex.copy(value = selectedFileTabIndex.value - 1)
                    } else if (selectedFileTabIndex.value == index && selectedFileTabIndex.value - 1 >= 0) {
                        selectedFileTabIndex.copy(value = selectedFileTabIndex.value - 1)
                    } else {
                        selectedFileTabIndex.copy()
                    }
                    fileTabItems.removeAt(index)
                    if (selectedFileTabIndex.value < fileTabItems.size) {
                        editingFile(fileTabItems[selectedFileTabIndex.value].file)
                    } else {
                        editingFile(null)
                    }
                    break
                }
            }
        }
    }

    private fun openFile(openFile: File) {
        viewModelScope.launch {
            val currentOpenningFile = fileTabItems.getOrNull(selectedFileTabIndex.value)
            currentOpenningFile?.apply {
                if (file.absolutePath == openFile.absolutePath) {
                    return@launch
                }
            }

            var openFileName: String = openFile.name
            for ((index, fileTab) in fileTabItems.withIndex()) {
                if (fileTab.file.absolutePath == openFile.absolutePath) {
                    selectedFileTabIndex = selectedFileTabIndex.copy(index)
                    return@launch
                }
                if (fileTab.name == openFile.name) {
                    fileTab.name = (fileTab.file.parentFile?.name ?: "") + "/${fileTab.file.name}"
                    openFileName = (openFile.parentFile?.name ?: "") + "/${openFileName}"
                }
            }

            fileTabItems.add(FileTabItem(openFile, openFileName))
            selectedFileTabIndex = selectedFileTabIndex.copy(fileTabItems.lastIndex)
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

            val removedList = buildList {
                for (fileTabItem in fileTabItems) {
                    if (!fileTabItem.file.exists()) {
                        add(fileTabItem)
                    }
                }
            }
            if (selectedFileTabIndex.value >= fileTabItems.size - removedList.size) {
                selectedFileTabIndex =
                    selectedFileTabIndex.copy(fileTabItems.size - removedList.size - 1)
            }
            fileTabItems.removeAll(removedList)

            loop@ for (removedFileTabItem in removedList) {
                val removedName = removedFileTabItem.name
                val removedIndex = removedFileTabItem.name.lastIndexOf('/')

                for (fileTabItem in fileTabItems) {
                    val fileName = fileTabItem.name
                    val currentIndex = fileTabItem.name.lastIndexOf('/')

                    if (removedIndex == -1 || currentIndex == -1) {
                        continue@loop
                    }

                    if (removedName.substring(
                            removedIndex + 1,
                            removedName.length
                        ) == fileName.substring(
                            currentIndex + 1,
                            fileName.length
                        )
                    ) {
                        fileTabItem.name =
                            fileName.substring(currentIndex + 1, fileName.length)
                    }
                }
            }
        }
    }

}