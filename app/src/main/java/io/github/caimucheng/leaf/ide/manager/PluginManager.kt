package io.github.caimucheng.leaf.ide.manager

import io.github.caimucheng.leaf.common.application.AppContext
import io.github.caimucheng.leaf.plugin.model.Plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PluginManager {

    private val plugins = ArrayList<Plugin>()

    suspend fun findInstalledPlugins() {
        return withContext(Dispatchers.IO) {
            val applications = AppContext.context.packageManager.getInstalledApplications(0)
            for (application in applications) {
                val metaData = application.metaData

            }
        }
    }

}