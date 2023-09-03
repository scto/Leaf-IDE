package io.github.caimucheng.leaf.ide.manager

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import dalvik.system.PathClassLoader
import io.github.caimucheng.leaf.common.application.AppContext
import io.github.caimucheng.leaf.plugin.PluginConfiguration
import io.github.caimucheng.leaf.plugin.PluginMain
import io.github.caimucheng.leaf.plugin.PluginProject
import io.github.caimucheng.leaf.plugin.model.Plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

object PluginManager {

    private val mutex = Mutex()

    private val plugins = ArrayList<Plugin>()

    suspend fun fetchPlugins() {
        return withContext(Dispatchers.IO) {
            mutex.lock()
            try {
                // Clear list
                plugins.clear()

                val context = AppContext.context
                val packageManager = context.packageManager
                val applications = packageManager.getInstalledApplications(
                    PackageManager.GET_META_DATA
                )
                for (application in applications) {
                    val name = application.loadLabel(packageManager).toString()
                    val icon = application.loadIcon(packageManager)
                    val packageName = application.packageName
                    val packageInfo = packageManager.getPackageInfo(packageName, 0)
                    val versionName = packageInfo.versionName
                    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        packageInfo.longVersionCode
                    } else {
                        @Suppress("DEPRECATION")
                        packageInfo.versionCode.toLong()
                    }
                    val metaData = application.metaData ?: continue
                    val pluginConfigurationPackageName = metaData.getString("pluginConfiguration")
                    val pluginMainPackageName = metaData.getString("pluginMain")
                    val pluginProjectPackageName = metaData.getString("pluginProject")
                    if (pluginConfigurationPackageName == null || pluginMainPackageName == null) {
                        continue
                    }
                    val resources = packageManager.getResourcesForApplication(application)
                    val pluginContext = context.createPackageContext(
                        packageName,
                        Context.CONTEXT_INCLUDE_CODE or Context.CONTEXT_IGNORE_SECURITY
                    )
                    val classLoader = PathClassLoader(
                        pluginContext.packageResourcePath,
                        context.classLoader
                    )
                    var pluginConfiguration: PluginConfiguration
                    var pluginMain: PluginMain
                    var pluginProject: PluginProject? = null

                    try {
                        val pluginConfigurationClass =
                            classLoader.loadClass(pluginConfigurationPackageName)
                        pluginConfiguration =
                            pluginConfigurationClass.getDeclaredConstructor()
                                .newInstance() as PluginConfiguration
                    } catch (e: Exception) {
                        e.printStackTrace()
                        continue
                    }

                    try {
                        val pluginMainClass =
                            classLoader.loadClass(pluginMainPackageName)
                        pluginMain =
                            pluginMainClass.getDeclaredConstructor().newInstance() as PluginMain
                    } catch (e: Exception) {
                        e.printStackTrace()
                        continue
                    }

                    if (pluginProjectPackageName != null) {
                        try {
                            val pluginProjectClass =
                                classLoader.loadClass(pluginProjectPackageName)
                            pluginProject =
                                pluginProjectClass.getDeclaredConstructor()
                                    .newInstance() as PluginProject
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    pluginConfiguration.setResources(resources)
                    pluginMain.setResources(resources)
                    pluginProject?.setResources(resources)

                    plugins.add(
                        Plugin(
                            name,
                            icon,
                            packageName,
                            versionName,
                            versionCode,
                            pluginConfiguration,
                            pluginMain,
                            pluginProject
                        )
                    )
                }
            } finally {
                mutex.unlock()
            }
        }
    }

    fun getPlugins(): List<Plugin> {
        return plugins
    }

    fun getPluginByPackageName(packageName: String): Plugin? {
        var result: Plugin? = null
        for (plugin in plugins) {
            if (plugin.packageName == packageName) {
                result = plugin
                break
            }
        }
        return result
    }

}