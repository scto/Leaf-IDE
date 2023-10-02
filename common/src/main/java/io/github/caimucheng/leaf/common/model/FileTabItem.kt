package io.github.caimucheng.leaf.common.model

import io.github.rosemoe.sora.text.CharPosition
import java.io.File

data class FileTabItem(
    var file: File,
    var name: String,
    var cursorPosition: CharPosition
)