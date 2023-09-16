package io.github.caimucheng.leaf.ide.model

import android.graphics.drawable.Drawable
import io.github.caimucheng.leaf.ide.manager.PluginManager
import io.github.caimucheng.leaf.plugin.PluginConfiguration
import io.github.caimucheng.leaf.plugin.PluginMain
import io.github.caimucheng.leaf.plugin.PluginProject

data class Plugin(
    val icon: Drawable,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val configuration: PluginConfiguration,
    val main: PluginMain,
    val project: PluginProject?
) {
    val name: String
        get() {
            return configuration.getResources().getString(configuration.pluginNameId())
        }

    val isSupported: Boolean
        get() {
            return configuration.targetPluginApi() == PluginManager.CURRENT_PLUGIN_API
        }

}