package io.github.caimucheng.leaf.plugin.manager

import io.github.caimucheng.leaf.common.util.LeafIDEProjectPath
import io.github.caimucheng.leaf.plugin.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Properties

object ProjectManager {

    private val projects = ArrayList<Project>()

    private val mutex = Mutex()

    private const val NAME = "name"

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
                    parseWorkspace(workspaceFile)?.also { projects.add(it) }
                }
            } finally {
                mutex.unlock()
            }
        }
    }

    private fun parseWorkspace(workspaceFile: File): Project? {
        val properties = Properties()
        workspaceFile.inputStream().use {
            properties.loadFromXML(it)
        }
        val propertyNames = properties.stringPropertyNames()
        var name: String? = null
        var plugin: String? = null
        val extraData = HashMap<String, Any?>()
        for (propertyName in propertyNames) {
            when (propertyName) {
                NAME -> name = properties.getProperty(propertyName)
                PLUGIN -> plugin = properties.getProperty(propertyName)
                else -> extraData[propertyName] = properties.getProperty(propertyName)
            }
        }
        if (name == null || plugin == null) {
            return null
        }
        val actualPlugin = PluginManager.getPluginByPackageName(plugin) ?: return null
        if (actualPlugin.project == null) {
            return null
        }
        return Project(
            name,
            actualPlugin,
            extraData
        )
    }

    fun getProjects(): List<Project> {
        return projects
    }

}