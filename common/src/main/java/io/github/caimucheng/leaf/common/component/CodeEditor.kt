package io.github.caimucheng.leaf.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.widget.CodeEditor

@Composable
fun CodeEditor(modifier: Modifier = Modifier) {
    AndroidView(
        factory = {
            CodeEditor(it)
        },
        modifier = modifier
    ) {

    }
}