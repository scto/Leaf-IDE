package io.github.caimucheng.leaf.ide.manager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.caimucheng.leaf.common.icon.JavaScriptFile
import io.github.caimucheng.leaf.common.icon.JsonFile
import io.github.caimucheng.leaf.common.icon.LeafFolder
import io.github.caimucheng.leaf.common.icon.TypeScriptExtensionFile
import io.github.caimucheng.leaf.common.icon.TypeScriptFile
import io.github.caimucheng.leaf.common.icon.XmlFile
import java.io.File

object IconManager {

    fun getFileIcon(file: File): ImageVector {
        return if (file.isFile) getFileTypeIcon(file) else getFolderTypeIcon(file)
    }

    private fun getFileTypeIcon(file: File): ImageVector {
        return when (file.extension) {
            "js" -> Icons.Filled.JavaScriptFile
            "ts" -> Icons.Filled.TypeScriptFile
            "tsx" -> Icons.Filled.TypeScriptExtensionFile
            "json" -> Icons.Filled.JsonFile
            "xml" -> Icons.Filled.XmlFile
            else -> Icons.Filled.Description
        }
    }

    private fun getFolderTypeIcon(file: File): ImageVector {
        return when (file.name) {
            ".LeafIDE" -> Icons.Filled.LeafFolder
            else -> Icons.Filled.Folder
        }
    }

}