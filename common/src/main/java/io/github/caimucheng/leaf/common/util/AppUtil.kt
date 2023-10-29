@file:Suppress("PrivatePropertyName")

package io.github.caimucheng.leaf.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.File
import kotlin.properties.Delegates

val Context.SettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val MaterialYouEnabledKey = booleanPreferencesKey("material_you_enabled")
val AutoDarkLightThemeKey = booleanPreferencesKey("auto_dark_light_theme")
val AppBrightnessKey = booleanPreferencesKey("app_brightness")
val EditorColorSchemeKey = stringPreferencesKey("editor_color_scheme")
val LigatureKey = booleanPreferencesKey("ligature")
val WordWrapKey = booleanPreferencesKey("word_wrap")
val SymbolInputBarKey = stringSetPreferencesKey("symbol_input_bar")
val ShowSymbolInputBarKey = booleanPreferencesKey("show_symbol_input_bar")
val InsertIndentSymbolKey = booleanPreferencesKey("insert_indent_symbol")
val MagnifierKey = booleanPreferencesKey("magnifier")
val OverScrollKey = booleanPreferencesKey("over_scroll")
val UseICULibKey = booleanPreferencesKey("use_icu_lib")
val DisplayConfigurationDirKey = booleanPreferencesKey("display_configuration_dir")

const val LAUNCH_MODE = "launch_mode"

val DEFAULT_SYMBOL_INPUT_BAR = setOf(
    "<", ">", "/", "=", "\"", ":", ";", "(",
    ")", ",", ".", "$", "?", "|", "\\", "&",
    "!", "[", "]", "{", "}", "_", "-"
)

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
    return sharedPreferences.getString(LAUNCH_MODE, null) ?: "unknown"
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

inline val Color.invert: Color
    get() {
        ColorSpaces.Srgb
        return Color(
            1f - red, 1f - green, 1f - blue, alpha
        )
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