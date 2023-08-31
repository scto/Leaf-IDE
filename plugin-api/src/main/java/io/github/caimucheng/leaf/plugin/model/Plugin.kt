package io.github.caimucheng.leaf.plugin.model

import io.github.caimucheng.leaf.plugin.PluginConfiguration
import io.github.caimucheng.leaf.plugin.PluginMain
import io.github.caimucheng.leaf.plugin.PluginProject

data class Plugin(
    val name: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val configuration: PluginConfiguration,
    val main: PluginMain,
    val project: PluginProject?
)