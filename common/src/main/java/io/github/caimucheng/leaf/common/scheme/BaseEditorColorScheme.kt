package io.github.caimucheng.leaf.common.scheme

import androidx.compose.ui.graphics.Color
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

@Suppress("LeakingThis")
open class BaseEditorColorScheme(
    protected val handleColor: Color?,
    protected val selectionBackgroundColor: Color?
) : EditorColorScheme() {

    init {
        applyDefault()
    }

    override fun applyDefault() {
        if (
            handleColor == null ||
            selectionBackgroundColor == null
        ) {
            return
        }
        super.applyDefault()
    }

}