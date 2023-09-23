package io.github.caimucheng.leaf.plugin

import android.content.res.Resources

abstract class PluginBase {

    private lateinit var resources: Resources

    private lateinit var packageName: String

    private lateinit var onRefresh: () -> Unit

    fun setResources(resources: Resources) {
        this.resources = resources
    }

    fun getResources(): Resources {
        return resources
    }

    fun stringResource(id: Int): String {
        return getResources().getString(id)
    }

    fun setPackageName(packageName: String) {
        this.packageName = packageName
    }

    fun getPackageName(): String {
        return packageName
    }

    fun setOnRefresh(onRefresh: () -> Unit) {
        this.onRefresh = onRefresh
    }

    fun refresh() {
        onRefresh()
    }

}