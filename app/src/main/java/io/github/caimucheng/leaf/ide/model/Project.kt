package io.github.caimucheng.leaf.ide.model

data class Project(
    val name: String,
    val description: String,
    val path: String,
    val plugin: Plugin,
    val extraData: Map<String, String>
)