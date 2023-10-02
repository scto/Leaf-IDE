package io.github.caimucheng.leaf.common.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.caimucheng.leaf.common.model.CodeEditorState
import io.github.rosemoe.sora.widget.CodeEditor

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    codeEditorState: CodeEditorState = rememberCodeEditorState()
) {
    AndroidView(
        factory = {
            CodeEditor(it)
        },
        modifier = modifier.fillMaxSize(),
        onRelease = {
            it.release()
        },
        update = {
            Log.e("update", "Y")
        }
    )
}

@Composable
fun rememberCodeEditorState(): CodeEditorState {
    return remember {
        CodeEditorState()
    }
}