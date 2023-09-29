package io.github.caimucheng.leaf.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.caimucheng.leaf.common.model.CodeEditorData
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.event.EventReceiver
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.subscribeEvent

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    content: Content = Content(),
    contentChange: EventReceiver<ContentChangeEvent>? = null,
    cursorLine: Int = 0,
    cursorColumn: Int = 0,
    update: CodeEditor.() -> Unit = {}
) {
    val codeEditorData = rememberCodeEditorData()
    AndroidView(
        factory = {
            CodeEditor(it)
        },
        modifier = modifier
    ) {
        if (it.text !== content) {
            it.setText(content)
        }
        if (contentChange != null && !codeEditorData.isReceivedContentChange) {
            it.subscribeEvent(contentChange)
            codeEditorData.isReceivedContentChange = true
        }
        val leftLine = it.cursor.leftLine
        val leftColumn = it.cursor.leftColumn
        if (cursorLine < it.lineCount) {
            if (cursorLine != leftLine && cursorColumn != leftColumn) {
                if (cursorColumn < it.text.getColumnCount(cursorLine)) {
                    it.setSelection(cursorLine, cursorColumn)
                }
            } else if (cursorLine != leftLine) {
                it.setSelection(cursorLine, leftColumn)
            } else if (cursorColumn != leftColumn) {
                if (cursorColumn < it.text.getColumnCount(cursorLine)) {
                    it.setSelection(leftLine, cursorColumn)
                }
            }
        }
        it.update()
    }
}

@Composable
private fun rememberCodeEditorData(): CodeEditorData {
    return remember {
        CodeEditorData()
    }
}