package io.github.caimucheng.leaf.common.scheme

import androidx.compose.material3.ColorScheme
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

    }
}