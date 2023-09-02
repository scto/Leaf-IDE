package io.github.caimucheng.leaf.plugin

import android.content.res.Resources

abstract class PluginConfiguration {

    private lateinit var resources: Resources

    open fun setResources(resources: Resources) {
        this.resources = resources
    }

    open fun getResources(): Resources {
        return resources
    }

    abstract fun enabled(): Boolean

    abstract fun descriptionId(): Int

    open fun author(): String? {
        return null
    }

}