package io.github.caimucheng.leaf.plugin

abstract class PluginConfiguration : PluginBase() {

    abstract fun pluginNameId(): Int

    abstract fun descriptionId(): Int

    open fun author(): String? {
        return null
    }

    open fun targetPluginApi(): Int {
        return 1
    }

}