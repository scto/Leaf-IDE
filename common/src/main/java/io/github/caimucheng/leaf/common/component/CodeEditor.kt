package io.github.caimucheng.leaf.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    content: Content = Content(),
    onUpdate: CodeEditor.() -> Unit = {}
) {
    AndroidView(
        factory = {
            CodeEditor(it)
        },
        modifier = modifier
    ) {
        if (it.text !== content) {
            it.setText(content)
        }
        it.onUpdate()
    }
}