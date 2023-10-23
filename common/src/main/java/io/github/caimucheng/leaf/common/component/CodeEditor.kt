package io.github.caimucheng.leaf.common.component

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.event.SelectionChangeEvent
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import io.github.rosemoe.sora.widget.subscribeEvent

@SuppressLint("UnrememberedMutableState")
@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    content: Content = Content(),
    cursorPosition: CharPosition = CharPosition(),
    typefacePath: String = "font/JetBrainsMono-Regular.ttf",
    colorScheme: EditorColorScheme,
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
            .also {
                val typeface = Typeface.createFromAsset(context.assets, typefacePath)
                it.typefaceText = typeface
            }
            .also {
                it.colorScheme = colorScheme
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
    LaunchedEffect(key1 = colorScheme) {
        if (codeEditor.colorScheme !== colorScheme) {
            codeEditor.colorScheme = colorScheme
        }
    }
    LaunchedEffect(key1 = content) {
        codeEditor.setText(content)
    }
    LaunchedEffect(key1 = cursorPosition) {
        val text = codeEditor.text
        val cursor = codeEditor.cursor
        val line = cursor.leftLine
        val column = cursor.leftColumn
        if (cursor.isSelected || line != cursorPosition.line || column != cursorPosition.column) {
            codeEditor.isCursorAnimationEnabled = false
            if (cursorPosition.line < text.lineCount && cursorPosition.column <= text.getColumnCount(
                    cursorPosition.line
                )
            ) {
                codeEditor.setSelection(cursorPosition.line, cursorPosition.column)
            } else {
                codeEditor.setSelection(0, 0)
            }
            codeEditor.isCursorAnimationEnabled = true
        }
    }

}

@Stable
class CodeEditorController(
    private val editor: CodeEditor
) {

    fun canUndo() = editor.canUndo()

    fun canRedo() = editor.canRedo()

    fun undo() = editor.undo()

    fun redo() = editor.redo()

}