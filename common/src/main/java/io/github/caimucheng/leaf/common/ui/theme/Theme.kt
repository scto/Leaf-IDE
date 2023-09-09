package io.github.caimucheng.leaf.common.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme()

val ModernLightColorScheme = lightColorScheme(
    Color(0.31764707f, 0.35686275f, 0.57254905f, 1.0f),
    Color(1.0f, 1.0f, 1.0f, 1.0f),
    Color(0.87058824f, 0.8784314f, 1.0f, 1.0f),
    Color(0.043137256f, 0.08235294f, 0.29411766f, 1.0f),
    Color(0.7294118f, 0.7647059f, 1.0f, 1.0f),
    Color(0.35686275f, 0.3647059f, 0.44705883f, 1.0f),
    Color(1.0f, 1.0f, 1.0f, 1.0f),
    Color(0.8784314f, 0.88235295f, 0.9764706f, 1.0f),
    Color(0.09411765f, 0.101960786f, 0.17254902f, 1.0f),
    Color(0.46666667f, 0.3254902f, 0.42745098f, 1.0f),
    Color(1.0f, 1.0f, 1.0f, 1.0f),
    Color(1.0f, 0.84313726f, 0.94509804f, 1.0f),
    Color(0.1764706f, 0.07058824f, 0.15686275f, 1.0f),
    Color(0.99607843f, 0.9843137f, 1.0f, 1.0f),
    Color(0.105882354f, 0.105882354f, 0.12156863f, 1.0f),
    Color(0.99607843f, 0.9843137f, 1.0f, 1.0f),
    Color(0.105882354f, 0.105882354f, 0.12156863f, 1.0f),
    Color(0.8901961f, 0.88235295f, 0.9254902f, 1.0f),
    Color(0.27450982f, 0.27450982f, 0.30980393f, 1.0f),
    Color(0.1882353f, 0.1882353f, 0.20392157f, 1.0f),
    Color(0.9529412f, 0.9411765f, 0.95686275f, 1.0f),
    Color(0.45882353f, 0.45882353f, 0.49803922f, 1.0f)
)

val ModernDarkColorScheme = darkColorScheme(
    Color(0.7294118f, 0.7647059f, 1.0f, 1.0f),
    Color(0.13333334f, 0.17254902f, 0.38039216f, 1.0f),
    Color(0.22352941f, 0.2627451f, 0.4745098f, 1.0f),
    Color(0.87058824f, 0.8784314f, 1.0f, 1.0f),
    Color(0.31764707f, 0.35686275f, 0.57254905f, 1.0f),
    Color(0.7647059f, 0.77254903f, 0.8666667f, 1.0f),
    Color(0.1764706f, 0.18431373f, 0.25882354f, 1.0f),
    Color(0.2627451f, 0.27450982f, 0.34901962f, 1.0f),
    Color(0.8784314f, 0.88235295f, 0.9764706f, 1.0f),
    Color(0.9019608f, 0.7294118f, 0.84313726f, 1.0f),
    Color(0.26666668f, 0.14901961f, 0.23921569f, 1.0f),
    Color(0.3647059f, 0.23529412f, 0.33333334f, 1.0f),
    Color(1.0f, 0.84313726f, 0.94509804f, 1.0f),
    Color(0.105882354f, 0.105882354f, 0.12156863f, 1.0f),
    Color(0.89411765f, 0.88235295f, 0.9019608f, 1.0f),
    Color(0.105882354f, 0.105882354f, 0.12156863f, 1.0f),
    Color(0.89411765f, 0.88235295f, 0.9019608f, 1.0f),
    Color(0.27450982f, 0.27450982f, 0.30980393f, 1.0f),
    Color(0.78039217f, 0.77254903f, 0.8156863f, 1.0f),
    Color(0.89411765f, 0.88235295f, 0.9019608f, 1.0f),
    Color(0.1882353f, 0.1882353f, 0.20392157f, 1.0f),
    Color(0.5647059f, 0.5647059f, 0.6039216f, 1.0f)
)

@Composable
fun LeafIDETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> ModernDarkColorScheme
        else -> ModernLightColorScheme
    }

    Log.e(
        "light", """
        ${colorScheme.primary}
        ${colorScheme.onPrimary}
        ${colorScheme.primaryContainer}
        ${colorScheme.onPrimaryContainer}
        ${colorScheme.inversePrimary}
        ${colorScheme.secondary}
        ${colorScheme.onSecondary}
        ${colorScheme.secondaryContainer}
        ${colorScheme.onSecondaryContainer}
        ${colorScheme.tertiary}
        ${colorScheme.onTertiary}
        ${colorScheme.tertiaryContainer}
        ${colorScheme.onTertiaryContainer}
        ${colorScheme.background}
        ${colorScheme.onBackground}
        ${colorScheme.surface}
        ${colorScheme.onSurface}
        ${colorScheme.surfaceVariant}
        ${colorScheme.onSurfaceVariant}
        ${colorScheme.inverseSurface}
        ${colorScheme.inverseOnSurface}
        ${colorScheme.outline}
    """.trimIndent()
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Color.toHexString(): String {
    val intColor = android.graphics.Color.argb(alpha, red, green, blue)
    return intColor.toString(16)
}