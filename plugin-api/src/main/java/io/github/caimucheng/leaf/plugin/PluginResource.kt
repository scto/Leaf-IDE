package io.github.caimucheng.leaf.plugin

import android.content.res.Resources

abstract class PluginResource {

    private lateinit var resources: Resources

    fun setResources(resources: Resources) {
        this.resources = resources
    }

    fun getResources(): Resources {
        return resources
    }

    fun stringResource(id: Int): String {
        return getResources().getString(id)
    }

}