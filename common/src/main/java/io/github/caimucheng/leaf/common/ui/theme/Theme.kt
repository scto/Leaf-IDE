package io.github.caimucheng.leaf.common.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.toFontFamily
import androidx.core.view.WindowCompat
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.PreferenceRequest
import io.github.caimucheng.leaf.common.util.AppBrightnessKey
import io.github.caimucheng.leaf.common.util.AutoDarkLightThemeKey
import io.github.caimucheng.leaf.common.util.MaterialYouEnabledKey
import io.github.caimucheng.leaf.common.util.SettingsDataStore
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val ModernLightColorScheme = lightColorScheme(
    Color(0xff4d5c9200000000UL),
    Color(0xffffffff00000000UL),
    Color(0xffdce1ff00000000UL),
    Color(0xff03174b00000000UL),
    Color(0xffb6c4ff00000000UL),
    Color(0xff595d7200000000UL),
    Color(0xffffffff00000000UL),
    Color(0xffdee1f900000000UL),
    Color(0xff161b2c00000000UL),
    Color(0xff75546f00000000UL),
    Color(0xffffffff00000000UL),
    Color(0xffffd7f600000000UL),
    Color(0xff2c122a00000000UL),
    Color(0xfffefbff00000000UL),
    Color(0xff1b1b1f00000000UL),
    Color(0xfffefbff00000000UL),
    Color(0xff1b1b1f00000000UL),
    Color(0xffe2e1ec00000000UL),
    Color(0xff45464f00000000UL),
    Color(0xff4d5c9200000000UL),
    Color(0xff30303400000000UL),
    Color(0xfff2f0f400000000UL),
    Color(0xffb3261e00000000UL),
    Color(0xffffffff00000000UL),
    Color(0xfff9dedc00000000UL),
    Color(0xff410e0b00000000UL),
    Color(0xff75757f00000000UL),
    Color(0xffcac4d000000000UL),
    Color(0xff00000000000000UL)
)

val ModernDarkColorScheme = darkColorScheme(
    Color(0xffb6c4ff00000000UL),
    Color(0xff1d2d6100000000UL),
    Color(0xff35447900000000UL),
    Color(0xffdce1ff00000000UL),
    Color(0xff4d5c9200000000UL),
    Color(0xffc2c5dd00000000UL),
    Color(0xff2b304200000000UL),
    Color(0xff42465900000000UL),
    Color(0xffdee1f900000000UL),
    Color(0xffe3bada00000000UL),
    Color(0xff43274000000000UL),
    Color(0xff5b3d5700000000UL),
    Color(0xffffd7f600000000UL),
    Color(0xff1b1b1f00000000UL),
    Color(0xffe4e1e600000000UL),
    Color(0xff1b1b1f00000000UL),
    Color(0xffe4e1e600000000UL),
    Color(0xff45464f00000000UL),
    Color(0xffc6c6d000000000UL),
    Color(0xffb6c4ff00000000UL),
    Color(0xffe4e1e600000000UL),
    Color(0xff30303400000000UL),
    Color(0xfff2b8b500000000UL),
    Color(0xff60141000000000UL),
    Color(0xff8c1d1800000000UL),
    Color(0xfff9dedc00000000UL),
    Color(0xff90909a00000000UL),
    Color(0xff49454f00000000UL),
    Color(0xff00000000000000UL)
)

private var _isDark = false

val isDark: Boolean
    get() {
        return _isDark
    }

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LeafIDETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dataStoreManager = DataStoreManager(LocalContext.current.SettingsDataStore)
    val materialYouRequest = PreferenceRequest(
        key = MaterialYouEnabledKey,
        defaultValue = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    )
    val autoDarkLightThemeRequest = PreferenceRequest(
        key = AutoDarkLightThemeKey,
        defaultValue = true
    )
    val appBrightnessRequest = PreferenceRequest(
        key = AppBrightnessKey,
        defaultValue = false
    )
    var materialYouEnabled by remember {
        mutableStateOf(runBlocking {
            dataStoreManager.getPreference(materialYouRequest)
        })
    }
    var autoDarkLightThemeEnabled by remember {
        mutableStateOf(runBlocking {
            dataStoreManager.getPreference(autoDarkLightThemeRequest)
        })
    }
    var appBrightnessEnabled by remember {
        mutableStateOf(runBlocking {
            dataStoreManager.getPreference(appBrightnessRequest)
        })
    }

    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        dataStoreManager.getPreferenceFlow(materialYouRequest)
            .distinctUntilChanged()
            .onEach {
                if (materialYouEnabled != it) {
                    materialYouEnabled = it
                }
            }.launchIn(this)
        dataStoreManager.getPreferenceFlow(autoDarkLightThemeRequest)
            .distinctUntilChanged()
            .onEach {
                if (autoDarkLightThemeEnabled != it) {
                    autoDarkLightThemeEnabled = it
                }
            }.launchIn(this)
        dataStoreManager.getPreferenceFlow(appBrightnessRequest)
            .distinctUntilChanged()
            .onEach {
                if (appBrightnessEnabled != it) {
                    appBrightnessEnabled = it
                }
            }
            .launchIn(this)
    }

    val colorScheme = when {
        materialYouEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (autoDarkLightThemeEnabled) {
                _isDark = darkTheme
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                _isDark = appBrightnessEnabled
                if (appBrightnessEnabled) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                    context
                )
            }
        }

        else -> {
            if (autoDarkLightThemeEnabled) {
                _isDark = darkTheme
                if (darkTheme) ModernDarkColorScheme else ModernLightColorScheme
            } else {
                _isDark = appBrightnessEnabled
                if (appBrightnessEnabled) ModernDarkColorScheme else ModernLightColorScheme
            }
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                if (autoDarkLightThemeEnabled) {
                    !darkTheme
                } else {
                    !appBrightnessEnabled
                }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography(),
        content = content
    )
}

@Composable
private fun typography(): Typography {
    if (_Typography != null) {
        return _Typography!!
    }

    _Typography = Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = fontFamily()),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = fontFamily()),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = fontFamily()),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = fontFamily()),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = fontFamily()),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = fontFamily()),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = fontFamily()),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = fontFamily()),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = fontFamily()),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = fontFamily()),
        bodyMedium = MaterialTheme.typography.bodyLarge.copy(fontFamily = fontFamily()),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = fontFamily()),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = fontFamily()),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = fontFamily()),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = fontFamily())
    )
    return _Typography!!
}

@Composable
private fun fontFamily(): FontFamily {
    if (_FontFamily != null) {
        return _FontFamily!!
    }

    _FontFamily = Font("font/MiSans-Regular.ttf", LocalContext.current.assets)
        .toFontFamily()
    return _FontFamily!!
}

private var _Typography: Typography? = null

private var _FontFamily: FontFamily? = null