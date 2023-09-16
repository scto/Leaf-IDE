package io.github.caimucheng.leaf.ide.manager

import io.github.caimucheng.leaf.common.util.Files
import io.github.caimucheng.leaf.common.util.LeafIDEProjectPath
import io.github.caimucheng.leaf.ide.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Properties

object ProjectManager {

    private val projects = ArrayList<Project>()

    private val mutex = Mutex()

    private const val NAME = "name"

    private const val DESCRIPTION = "description"

    private const val PLUGIN = "plugin"

    suspend fun fetchProjects() {
        return withContext(Dispatchers.IO) {
            mutex.lock()
            try {
                // Clear list
                projects.clear()

                val listFiles = LeafIDEProjectPath.listFiles() ?: emptyArray()
                for (file in listFiles) {
                    if (file.isFile) {
                        continue
                    }
                    val configurationDirectory = File(file, ".LeafIDE")
                    if (!configurationDirectory.exists() || configurationDirectory.isFile) {
                        continue
                    }
                    val workspaceFile = File(configurationDirectory, "workspace.xml")
                    if (!workspaceFile.exists() || workspaceFile.isDirectory) {
                        continue
                    }
                    parseWorkspace(file, workspaceFile)?.also { projects.add(it) }
                }
            } finally {
                mutex.unlock()
            }
        }
    }

    private fun parseWorkspace(rootFile: File, workspaceFile: File): Project? {
        if (!rootFile.exists() || rootFile.isFile) {
            return null
        }
        if (!workspaceFile.exists() || workspaceFile.isDirectory) {
            return null
        }
        val properties = Properties()
        workspaceFile.inputStream().use {
            properties.loadFromXML(it)
        }
        val propertyNames = properties.stringPropertyNames()
        var name: String? = null
        var description: String? = null
        var plugin: String? = null
        val extraData = HashMap<String, Any?>()
        for (propertyName in propertyNames) {
            when (propertyName) {
                NAME -> name = properties.getProperty(propertyName)
                DESCRIPTION -> description = properties.getProperty(description, null)
                PLUGIN -> plugin = properties.getProperty(propertyName)
                else -> extraData[propertyName] = properties.getProperty(propertyName)
            }
        }
        if (name == null || plugin == null) {
            return null
        }
        val actualPlugin = PluginManager.getPluginByPackageName(plugin) ?: return null
        if (actualPlugin.project == null || !actualPlugin.isSupported) {
            return null
        }
        return Project(
            name,
            description,
            rootFile.absolutePath,
            actualPlugin,
            extraData
        )
    }

    fun getProjects(): List<Project> {
        return projects
    }

    suspend fun deleteProject(project: Project) {
        return withContext(Dispatchers.IO) {
            mutex.lock()
            try {
                val rootFile = File(project.path)
                val configurationDirectory = File(project.path, ".LeafIDE")
                val workspaceFile = File(configurationDirectory, "workspace.xml")
                parseWorkspace(rootFile, workspaceFile) ?: return@withContext
                Files.delete(project.path)
            } finally {
                mutex.unlock()
            }
        }
    }

}