package io.github.caimucheng.leaf.plugin.model

import io.github.caimucheng.leaf.plugin.PluginMain

data class Plugin(
    val name: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val main: PluginMain,
)