package io.github.caimucheng.leaf.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.event.SelectionChangeEvent
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.subscribeEvent

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    content: Content = Content(),
    cursorPosition: CharPosition = CharPosition(),
    init: (CodeEditor) -> Unit = {}
) {
    val context = LocalContext.current
    val codeEditor = remember {
        CodeEditor(context)
            .also(init)
            .also {
                it.subscribeEvent<SelectionChangeEvent> { event, _ ->
                    cursorPosition.line = event.left.line
                    cursorPosition.column = event.left.column
                    cursorPosition.index = event.left.index
                }
            }
    }
    AndroidView(
        factory = {
            codeEditor
        },
        modifier = modifier,
        onRelease = {
            it.release()
        }
    )
    LaunchedEffect(key1 = content) {
        codeEditor.setText(content)
    }
    LaunchedEffect(key1 = cursorPosition) {
        val cursor = codeEditor.cursor
        val line = cursor.leftLine
        val column = cursor.leftColumn
        if (cursor.isSelected || line != cursorPosition.line || column != cursorPosition.column) {
            codeEditor.isCursorAnimationEnabled = false
            codeEditor.setSelection(cursorPosition.line, cursorPosition.column)
            codeEditor.isCursorAnimationEnabled = true
        }
    }
}