package io.github.caimucheng.leaf.common.scheme

import android.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.toArgb
import io.github.caimucheng.leaf.common.ui.theme.isDark
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

class DynamicEditorColorScheme(private val colorScheme: ColorScheme?) :
    EditorColorScheme(isDark) {

    init {
        applyDefault()
    }

    override fun applyDefault() {
        if (colorScheme == null) {
            return
        }
        super.applyDefault()
        if (isDark) {
            colorScheme.applyDark()
        } else {
            colorScheme.applyLight()
        }
    }

    private fun ColorScheme.applyLight() {
        setColor(WHOLE_BACKGROUND, "#FFFFFFFF".toColor())
        setColor(LINE_NUMBER_BACKGROUND, "#FFFFFFFF".toColor())
        setColor(LINE_NUMBER_CURRENT, primary.toArgb())
        setColor(LINE_NUMBER, onSurface.toArgb())
        setColor(TEXT_NORMAL, onSurface.toArgb())
    }

    private fun ColorScheme.applyDark() {
        setColor(WHOLE_BACKGROUND, "#FF1B1B1B".toColor())
        setColor(LINE_NUMBER_BACKGROUND, "#FF1B1B1B".toColor())
        setColor(LINE_NUMBER_CURRENT, primary.toArgb())
        setColor(LINE_NUMBER, onSurface.toArgb())
        setColor(TEXT_NORMAL, onSurface.toArgb())
    }

    private fun String.toColor() = Color.parseColor(this)

}