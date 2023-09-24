package io.github.caimucheng.leaf.ide.manager

import io.github.caimucheng.leaf.common.model.Workspace
import io.github.caimucheng.leaf.common.util.Files
import io.github.caimucheng.leaf.common.util.LeafIDEProjectPath
import io.github.caimucheng.leaf.ide.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

object ProjectManager {

    private val projects = ArrayList<Project>()

    suspend fun fetchProjects() {
        return withContext(Dispatchers.IO) {
            // Clear list
            projects.clear()

            val listFiles = LeafIDEProjectPath.listFiles() ?: emptyArray()
            listFiles.sortWith { first, second ->
                (second.lastModified() - first.lastModified()).toInt()
            }
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
        }
    }

    fun parseWorkspace(rootFile: File, workspaceFile: File): Project? {
        if (!rootFile.exists() || rootFile.isFile) {
            return null
        }
        if (!workspaceFile.exists() || workspaceFile.isDirectory) {
            return null
        }
        val properties = Properties()
        try {
            workspaceFile.inputStream().use {
                properties.loadFromXML(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val propertyNames = properties.stringPropertyNames()
        var name: String? = null
        var description: String? = null
        var plugin: String? = null
        val extraData = HashMap<String, String>()
        for (propertyName in propertyNames) {
            when (propertyName) {
                Workspace.NAME -> name = properties.getProperty(propertyName)
                Workspace.DESCRIPTION -> description = properties.getProperty(propertyName, "")
                Workspace.PLUGIN -> plugin = properties.getProperty(propertyName)
                else -> extraData[propertyName] = properties.getProperty(propertyName)
            }
        }
        if (name == null || description == null || plugin == null) {
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
            Files.delete(project.path)
            fetchProjects()
        }
    }

    suspend fun renameProject(project: Project, newName: String) {
        return withContext(Dispatchers.IO) {
            val fromPath = project.path
            val toPath = File(LeafIDEProjectPath, newName).absolutePath
            Files.rename(fromPath, toPath)

            val configurationFile = File(toPath, ".LeafIDE")
            if (configurationFile.exists() && configurationFile.isDirectory) {
                val workspaceFile = File(configurationFile, "workspace.xml")
                val workspace =
                    Workspace.loadFromXML(FileInputStream(workspaceFile)) ?: return@withContext
                workspace.name = newName
                workspace.storeToXML(FileOutputStream(workspaceFile))
            }
            fetchProjects()
        }
    }

}