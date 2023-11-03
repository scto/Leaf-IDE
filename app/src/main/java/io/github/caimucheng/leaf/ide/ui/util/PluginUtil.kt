package io.github.caimucheng.leaf.ide.ui.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.caimucheng.leaf.ide.application.AppContext
import io.github.caimucheng.leaf.plugin.PluginConfiguration

inline val PluginsSharedPreferences: SharedPreferences
    get() {
        return AppContext.context.getSharedPreferences("plugins_shared_prefernecs", Context.MODE_PRIVATE)
    }

fun PluginConfiguration.enabled(): Boolean {
    return PluginsSharedPreferences.getBoolean(
        "${getPackageName()}_enabled",
        true
    )
}

fun PluginConfiguration.enable() {
    PluginsSharedPreferences.edit {
        putBoolean(
            "${getPackageName()}_enabled",
            true
        )
    }
}

fun PluginConfiguration.disable() {
    PluginsSharedPreferences.edit {
        putBoolean(
            "${getPackageName()}_enabled",
            false
        )
    }
}

fun PluginConfiguration.toggle() {
    if (enabled()) {
        disable()
    } else {
        enable()
    }
}