package io.github.caimucheng.leaf.ide.model

import io.github.caimucheng.leaf.plugin.model.Plugin

data class Project(
    val name: String,
    val plugin: Plugin,
    val extraData: Map<String, Any?>
)