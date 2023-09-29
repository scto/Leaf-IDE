package io.github.caimucheng.leaf.ide.model

import io.github.caimucheng.leaf.common.model.Workspace

data class Project(
    val name: String,
    val description: String,
    val path: String,
    val plugin: Plugin,
    val extraData: Map<String, String>
) {

    fun toWorkspace(): Workspace {
        return Workspace(
            name = name,
            description = description,
            plugin = plugin.packageName,
            extraData
        )
    }

}