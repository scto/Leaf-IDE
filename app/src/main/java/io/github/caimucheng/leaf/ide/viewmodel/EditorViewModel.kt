package io.github.caimucheng.leaf.ide.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.BreadcrumbItem
import io.github.caimucheng.leaf.common.model.FileTabItem
import io.github.caimucheng.leaf.common.model.PreferenceRequest
import io.github.caimucheng.leaf.common.model.Value
import io.github.caimucheng.leaf.common.util.DisplayConfigurationDirKey
import io.github.caimucheng.leaf.common.util.Files
import io.github.caimucheng.leaf.common.util.LeafIDEProjectPath
import io.github.caimucheng.leaf.common.util.SettingsDataStore
import io.github.caimucheng.leaf.ide.application.AppContext
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.text.ContentIO
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset

sealed class EditorUIIntent {

    data class Refresh(val path: String) : EditorUIIntent()

    data class OpenFile(val file: File) : EditorUIIntent()

    data class CloseFile(val file: File) : EditorUIIntent()

    data class CloseOthers(val currentFile: File) : EditorUIIntent()

    data object CloseAll : EditorUIIntent()

    data class EditingFile(val file: File?, val cursorPosition: CharPosition = CharPosition()) :
        EditorUIIntent()

    data class SaveFile(val file: File?) : EditorUIIntent()

    data class CreateFile(val currentPath: String, val name: String, val isDirectory: Boolean) :
        EditorUIIntent()

    data class DeleteFile(val currentPath: String, val child: File) : EditorUIIntent()
    data class RenameFile(val currentPath: String, val file: File, val newName: String) :
        EditorUIIntent()

    data class SaveCursorState(val cursorPosition: CharPosition) : EditorUIIntent()

}

class EditorViewModel : ViewModel() {

    val breadcrumbItems: MutableList<BreadcrumbItem> = mutableStateListOf()

    val fileTabItems: MutableList<FileTabItem> = mutableStateListOf()

    var selectedBreadcrumbIndex: Int by mutableIntStateOf(0)

    var selectedFileTabIndex: Value<Int> by mutableStateOf(Value(0))

    val children: MutableList<File> = mutableStateListOf()

    var editingFile: File? by mutableStateOf(null)

    var loading: Boolean by mutableStateOf(false)

    val intent: Channel<EditorUIIntent> = Channel(Channel.UNLIMITED)

    var content by mutableStateOf(Content())

    var cursorPosition by mutableStateOf(CharPosition())

    init {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is EditorUIIntent.Refresh -> refresh(it.path)
                    is EditorUIIntent.OpenFile -> openFile(it.file)
                    is EditorUIIntent.CloseFile -> closeFile(it.file)
                    is EditorUIIntent.CloseOthers -> closeOthers(it.currentFile)
                    is EditorUIIntent.EditingFile -> editingFile(it.file, it.cursorPosition)
                    is EditorUIIntent.SaveFile -> saveFile(it.file)
                    is EditorUIIntent.CreateFile -> createFile(
                        it.currentPath,
                        it.name,
                        it.isDirectory
                    )

                    is EditorUIIntent.SaveCursorState -> saveCursorState(it.cursorPosition)
                    is EditorUIIntent.RenameFile -> renameFile(it.currentPath, it.file, it.newName)
                    is EditorUIIntent.DeleteFile -> deleteFile(it.currentPath, it.child)
                    EditorUIIntent.CloseAll -> closeAll()
                }
            }
        }
    }

    private fun saveCursorState(cursorPosition: CharPosition) {
        viewModelScope.launch {
            editingFile?.let { file ->
                val fileTabItem =
                    fileTabItems.find { fileTabItem -> fileTabItem.file.absolutePath == file.absolutePath }
                fileTabItem?.cursorPosition?.set(cursorPosition)
            }
        }
    }

    private fun renameFile(currentPath: String, file: File, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Files.rename(file, File(currentPath, newName))
            refresh(currentPath).join()
        }
    }

    private fun deleteFile(currentPath: String, child: File) {
        viewModelScope.launch(Dispatchers.IO) {
            Files.delete(child)
            refresh(currentPath).join()
        }
    }

    private fun createFile(currentPath: String, name: String, isDirectory: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(currentPath, name)
            if (isDirectory) {
                file.mkdir()
            } else {
                file.createNewFile()
            }
            refresh(currentPath).join()
        }
    }

    private fun saveFile(file: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            file?.let {
                try {
                    FileOutputStream(it).use { fileOutputStream ->
                        ContentIO.writeTo(content, fileOutputStream, Charset.forName("UTF-8"), true)
                    }
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        throw e
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun editingFile(file: File?, cursorPosition: CharPosition = CharPosition()) {
        loading = true
        viewModelScope.launch {
            file?.let {
                content = withContext(Dispatchers.IO) {
                    try {
                        FileInputStream(it).use {
                            ContentIO.createFrom(it, Charset.forName("UTF-8"))
                        }
                    } catch (e: Exception) {
                        if (e is CancellationException) {
                            throw e
                        }
                        e.printStackTrace()
                        Content()
                    }
                }
                this@EditorViewModel.cursorPosition = cursorPosition
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
                        editingFile(
                            fileTabItems[selectedFileTabIndex.value].file,
                            fileTabItems[selectedFileTabIndex.value].cursorPosition
                        )
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

            for ((index, fileTab) in fileTabItems.withIndex()) {
                if (fileTab.file.absolutePath == openFile.absolutePath) {
                    selectedFileTabIndex = selectedFileTabIndex.copy(index)
                    return@launch
                }
            }

            fileTabItems.add(FileTabItem(openFile, toRelativePath(openFile), CharPosition()))
            selectedFileTabIndex = selectedFileTabIndex.copy(fileTabItems.lastIndex)
        }
    }

    private fun toRelativePath(file: File): String {
        val trimPath = file.absolutePath.replace("${LeafIDEProjectPath.absolutePath}/", "")
        val index = trimPath.indexOf("/")
        if (index == -1) return file.name
        return trimPath.substring(index + 1)
    }

    private fun refresh(path: String): Job {
        children.clear()
        val dataStoreManager = DataStoreManager(AppContext.context.SettingsDataStore)
        val displayConfigurationDirRequest = PreferenceRequest(
            key = DisplayConfigurationDirKey,
            defaultValue = false
        )
        return viewModelScope.launch {
            val displayConfigurationDir = dataStoreManager.getPreference(
                displayConfigurationDirRequest
            )
            val configurationFile = File(path, ".LeafIDE")
            val file = File(path)
            val files = (file.listFiles() ?: emptyArray()).let {
                if (!displayConfigurationDir && configurationFile.exists() && configurationFile.isDirectory) {
                    it.filter { theFile -> theFile.absolutePath != configurationFile.absolutePath }
                        .toTypedArray()
                } else {
                    it
                }
            }
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
        }
    }

}