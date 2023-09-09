@file:Suppress("PrivatePropertyName")

package io.github.caimucheng.leaf.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.core.content.edit
import java.io.File
import kotlin.properties.Delegates
import kotlin.system.exitProcess

const val LAUNCH_MODE = "launch_mode"

val ExternalRootPath =
    Environment.getExternalStorageDirectory()!!

private var _LeafIDERootPath: File by Delegates.notNull()
val LeafIDERootPath: File
    get() {
        return _LeafIDERootPath
    }

private var _LeafIDEProjectPath: File by Delegates.notNull()
val LeafIDEProjectPath: File
    get() {
        return _LeafIDEProjectPath
    }

inline val Context.sharedPreferences: SharedPreferences
    get() {
        return getSharedPreferences("Settings", Context.MODE_PRIVATE)
    }

inline val Context.isExternalLaunchMode: Boolean
    get() {
        return launchMode() == "external"
    }

inline val Context.isInternalLaunchMode: Boolean
    get() {
        return launchMode() == "internal"
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

fun Context.setupMainActivity(launchMode: String) {
    when (launchMode) {
        "external" -> setupExternal()
        "internal" -> setupInternal()
    }
}

private fun setupExternal() {
    _LeafIDERootPath = File(ExternalRootPath, "LeafIDE")
    _LeafIDERootPath.mkdirs()

    setupCommonChildren()
}

private fun Context.setupInternal() {
    _LeafIDERootPath = File(filesDir, "LeafIDE")
    _LeafIDERootPath.mkdirs()

    setupCommonChildren()
}

fun Context.uninstallAPP(packageName: String) {
    val uri = Uri.fromParts("package", packageName, null)
    startActivity(Intent(Intent.ACTION_DELETE, uri))
}

private fun setupCommonChildren() {
    val nomediaFile = File(_LeafIDERootPath, ".nomedia")
    try {
        nomediaFile.createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    _LeafIDEProjectPath = File(_LeafIDERootPath, "projects")
    _LeafIDEProjectPath.mkdirs()
}