@file:Suppress("PrivatePropertyName")

package io.github.caimucheng.leaf.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.core.content.edit
import io.github.caimucheng.leaf.common.application.AppContext
import java.io.File
import java.io.IOException
import kotlin.properties.Delegates
import kotlin.system.exitProcess

const val LAUNCH_MODE = "launch_mode"

val ExternalRootPath = Environment.getExternalStorageDirectory()!!

private var _LeafIDERootPath: File by Delegates.notNull()
val LeafIDERootPath: File
    get() {
        return _LeafIDERootPath
    }

inline val Context.sharedPreferences: SharedPreferences
    get() {
        return getSharedPreferences("Settings", Context.MODE_PRIVATE)
    }

inline val isExternalLaunchMode: Boolean
    get() {
        return AppContext.context.launchMode() == "external"
    }

inline val isInternalLaunchMode: Boolean
    get() {
        return AppContext.context.launchMode() == "internal"
    }

inline val isRootLaunchMode: Boolean
    get() {
        return AppContext.context.launchMode() == "root"
    }

fun Context.launchMode(): String {
    return sharedPreferences.getString(LAUNCH_MODE, null) ?: exitProcess(1)
}

fun ComponentActivity.setupApp(launchMode: String, targetClass: Class<out Activity>) {
    sharedPreferences.edit {
        putString(LAUNCH_MODE, launchMode)
    }

    startActivity(Intent(this, targetClass))
    finish()
}

fun setupMainActivity(launchMode: String) {
    when (launchMode) {
        "external" -> setupExternal()
        "internal" -> setupInternal()
        "root" -> setupRoot()
    }
}

private fun setupExternal() {
    _LeafIDERootPath = File(ExternalRootPath, "LeafIDE")
    FileSystem.mkdirs(_LeafIDERootPath)
}

private fun setupInternal() {
    _LeafIDERootPath = File(AppContext.context.filesDir, "LeafIDE")
    FileSystem.mkdirs(_LeafIDERootPath)
}

private fun setupRoot() {
    _LeafIDERootPath = File("/data/adb/LeafIDE")
    FileSystem.mkdirs(_LeafIDERootPath)
}

fun ComponentActivity.setupRootApp(
    launchMode: String,
    targetClass: Class<out Activity>,
    onError: () -> Unit
) {
    try {
        // Request root permission
        val process = Runtime.getRuntime().exec("su -c /system/bin/id -u")
        process.waitFor()

        process.outputStream.use {
            process.inputStream.use {
                // "0\n".equals("0\n")
                if (process.inputStream.readBytes().contentEquals(byteArrayOf(48, 10))) {
                    setupApp(launchMode, targetClass)
                } else {
                    onError()
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        onError()
    }
}