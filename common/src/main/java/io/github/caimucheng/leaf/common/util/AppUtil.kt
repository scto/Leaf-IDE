@file:Suppress("PrivatePropertyName")

package io.github.caimucheng.leaf.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.caimucheng.leaf.common.model.UnwrappedColorScheme
import java.io.File
import kotlin.properties.Delegates

val Context.SettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val MaterialYouEnabledKey = booleanPreferencesKey("material_you_enabled")
val AutoDarkLightThemeKey = booleanPreferencesKey("auto_dark_light_theme")
val AppBrightnessKey = booleanPreferencesKey("app_brightness")
val DisplayConfigurationDirKey = booleanPreferencesKey("display_configuration_dir")

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
    return sharedPreferences.getString(LAUNCH_MODE, null) ?: "unknown"
}

fun unwrapColorScheme(colorScheme: ColorScheme): UnwrappedColorScheme {
    val primary = colorScheme.primary
    val onPrimary = colorScheme.onPrimary
    val primaryContainer = colorScheme.primaryContainer
    val onPrimaryContainer = colorScheme.onPrimaryContainer
    val inversePrimary = colorScheme.inversePrimary
    val secondary = colorScheme.secondary
    val onSecondary = colorScheme.onSecondary
    val secondaryContainer = colorScheme.secondaryContainer
    val onSecondaryContainer = colorScheme.onSecondaryContainer
    val tertiary = colorScheme.tertiary
    val onTertiary = colorScheme.onTertiary
    val tertiaryContainer = colorScheme.tertiaryContainer
    val onTertiaryContainer = colorScheme.onTertiaryContainer
    val background = colorScheme.background
    val onBackground = colorScheme.onBackground
    val surface = colorScheme.surface
    val onSurface = colorScheme.onSurface
    val surfaceVariant = colorScheme.surfaceVariant
    val onSurfaceVariant = colorScheme.onSurfaceVariant
    val surfaceTint = colorScheme.surfaceTint
    val inverseSurface = colorScheme.inverseSurface
    val inverseOnSurface = colorScheme.inverseOnSurface
    val error = colorScheme.error
    val onError = colorScheme.onError
    val errorContainer = colorScheme.errorContainer
    val onErrorContainer = colorScheme.onErrorContainer
    val outline = colorScheme.outline
    val outlineVariant = colorScheme.outlineVariant
    val scrim = colorScheme.scrim
    return UnwrappedColorScheme(
        primary.toArgb(),
        onPrimary.toArgb(),
        primaryContainer.toArgb(),
        onPrimaryContainer.toArgb(),
        inversePrimary.toArgb(),
        secondary.toArgb(),
        onSecondary.toArgb(),
        secondaryContainer.toArgb(),
        onSecondaryContainer.toArgb(),
        tertiary.toArgb(),
        onTertiary.toArgb(),
        tertiaryContainer.toArgb(),
        onTertiaryContainer.toArgb(),
        background.toArgb(),
        onBackground.toArgb(),
        surface.toArgb(),
        onSurface.toArgb(),
        surfaceVariant.toArgb(),
        onSurfaceVariant.toArgb(),
        surfaceTint.toArgb(),
        inverseSurface.toArgb(),
        inverseOnSurface.toArgb(),
        error.toArgb(),
        onError.toArgb(),
        errorContainer.toArgb(),
        onErrorContainer.toArgb(),
        outline.toArgb(),
        outlineVariant.toArgb(),
        scrim.toArgb()
    )
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