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

fun unwrapColorScheme(isDark: Boolean): UnwrappedColorScheme {
    val lightEditorColorScheme = UnwrappedColorScheme(
        stickyScrollDivider = Color(0xFFFFFFFF).toArgb(),
        strikethrough = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipAction = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipDetailedMsg = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipBriefMsg = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipBackground = Color(0xFFFFFFFF).toArgb(),
        functionCharBackgroundStroke = Color(0xFFFFFFFF).toArgb(),
        hardWrapMarker = Color(0xFFFFFFFF).toArgb(),
        textInlayHintForeground = Color(0xFFFFFFFF).toArgb(),
        textInlayHintBackground = Color(0xFFFFFFFF).toArgb(),
        snippetBackgroundEditing = Color(0xFFFFFFFF).toArgb(),
        snippetBackgroundRelated = Color(0xFFFFFFFF).toArgb(),
        snippetBackgroundInactive = Color(0xFFFFFFFF).toArgb(),
        sideBlockLine = Color(0xFFFFFFFF).toArgb(),
        nonPrintableChar = Color(0xFFFFFFFF).toArgb(),
        matchedTextBackground = Color(0xFFFFFFFF).toArgb(),
        completionWndCorner = Color(0xFFFFFFFF).toArgb(),
        completionWndBackground = Color(0xFFFFFFFF).toArgb(),
        completionWndTextPrimary = Color(0xFFFFFFFF).toArgb(),
        completionWndTextSecondary = Color(0xFFFFFFFF).toArgb(),
        completionWndItemCurrent = Color(0xFFFFFFFF).toArgb(),
        highlightedDelimitersBackground = Color(0xFFFFFFFF).toArgb(),
        highlightedDelimitersUnderline = Color(0xFFFFFFFF).toArgb(),
        highlightedDelimitersForeground = Color(0xFFFFFFFF).toArgb(),
        blockLineCurrent = Color(0xFFFFFFFF).toArgb(),
        blockLine = Color(0xFFFFFFFF).toArgb(),
        underline = Color(0xFFFFFFFF).toArgb(),

        lineNumberPanelText = Color(0xFFFFFFFF).toArgb(),
        lineNumberPanel = Color(0xFF000000).toArgb(),
        textSelected = Color(0xFF000000).toArgb(),
        scrollBarTrack = Color.Transparent.toArgb(),
        scrollBarThumbPressed = Color(0xFFD8D8D8).toArgb(),
        scrollBarThumb = Color(0xFFE2E2E2).toArgb(),
        currentLine = Color(0xFFFCFAED).toArgb(),
        selectionHandle = Color(0xFF5395F9).toArgb(),
        selectionInsert = Color(0xFF5395F9).toArgb(),
        selectedTextBackground = Color(0xFFA6D2FF).toArgb(),
        textNormal = Color(0xFF000000).toArgb(),
        wholeBackground = Color(0xFFFFFFFF).toArgb(),
        lineNumberBackground = Color(0xFFFFFFFF).toArgb(),
        lineNumberCurrent = Color(0xFFC3C3C3).toArgb(),
        lineNumber = Color(0xFFC3C3C3).toArgb(),
        lineDivider = Color(0xFFC3C3C3).toArgb(),
    )
    val darkEditorColorScheme = UnwrappedColorScheme(
        stickyScrollDivider = Color(0xFFFFFFFF).toArgb(),
        strikethrough = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipAction = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipDetailedMsg = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipBriefMsg = Color(0xFFFFFFFF).toArgb(),
        diagnosticTooltipBackground = Color(0xFFFFFFFF).toArgb(),
        functionCharBackgroundStroke = Color(0xFFFFFFFF).toArgb(),
        hardWrapMarker = Color(0xFFFFFFFF).toArgb(),
        textInlayHintForeground = Color(0xFFFFFFFF).toArgb(),
        textInlayHintBackground = Color(0xFFFFFFFF).toArgb(),
        snippetBackgroundEditing = Color(0xFFFFFFFF).toArgb(),
        snippetBackgroundRelated = Color(0xFFFFFFFF).toArgb(),
        snippetBackgroundInactive = Color(0xFFFFFFFF).toArgb(),
        sideBlockLine = Color(0xFFFFFFFF).toArgb(),
        nonPrintableChar = Color(0xFFFFFFFF).toArgb(),
        matchedTextBackground = Color(0xFFFFFFFF).toArgb(),
        completionWndCorner = Color(0xFFFFFFFF).toArgb(),
        completionWndBackground = Color(0xFFFFFFFF).toArgb(),
        completionWndTextPrimary = Color(0xFFFFFFFF).toArgb(),
        completionWndTextSecondary = Color(0xFFFFFFFF).toArgb(),
        completionWndItemCurrent = Color(0xFFFFFFFF).toArgb(),
        highlightedDelimitersBackground = Color(0xFFFFFFFF).toArgb(),
        highlightedDelimitersUnderline = Color(0xFFFFFFFF).toArgb(),
        highlightedDelimitersForeground = Color(0xFFFFFFFF).toArgb(),
        blockLineCurrent = Color(0xFFFFFFFF).toArgb(),
        blockLine = Color(0xFFFFFFFF).toArgb(),
        underline = Color(0xFFFFFFFF).toArgb(),

        lineNumberPanelText = Color(0xFFBCBEC4).toArgb(),
        lineNumberPanel = Color(0xFF2B2D30).toArgb(),
        textSelected = Color(0xFFBCBEC4).toArgb(),
        scrollBarTrack = Color.Transparent.toArgb(),
        scrollBarThumbPressed = Color(0xFFDCDEE2).toArgb(),
        scrollBarThumb = Color(0xFFBCBEC4).toArgb(),
        currentLine = Color(0xFF26282E).toArgb(),
        selectionHandle = Color(0xFF5395F9).toArgb(),
        selectionInsert = Color(0xFF5395F9).toArgb(),
        selectedTextBackground = Color(0xFF214283).toArgb(),
        textNormal = Color(0xFFBCBEC4).toArgb(),
        wholeBackground = Color(0xFF1E1F22).toArgb(),
        lineNumberBackground = Color(0xFF1E1F22).toArgb(),
        lineNumberCurrent = Color(0xFFF3F3F3).toArgb(),
        lineNumber = Color(0xFFBCBEC4).toArgb(),
        lineDivider = Color(0xFFBCBEC4).toArgb(),
    )
    return if (isDark) darkEditorColorScheme else lightEditorColorScheme
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