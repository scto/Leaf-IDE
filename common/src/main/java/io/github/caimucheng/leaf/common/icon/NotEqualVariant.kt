package io.github.caimucheng.leaf.common.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.NotEqualVariant: ImageVector
    get() {
        if (_notEqualVariant != null) {
            return _notEqualVariant!!
        }
        _notEqualVariant = materialIcon(
            name = "NotEqualVariant"
        ) {
            materialPath {
                moveTo(14.08F, 4.61F)
                lineTo(15.92F, 5.4F)
                lineTo(14.8F, 8.0F)
                horizontalLineTo(19.0F)
                verticalLineTo(10.0F)
                horizontalLineTo(13.95F)
                lineTo(12.23F, 14.0F)
                horizontalLineTo(19.0F)
                verticalLineTo(16.0F)
                horizontalLineTo(11.38F)
                lineTo(9.92F, 19.4F)
                lineTo(8.08F, 18.61F)
                lineTo(9.2F, 16.0F)
                horizontalLineTo(5.0F)
                verticalLineTo(14.0F)
                horizontalLineTo(10.06F)
                lineTo(11.77F, 10.0F)
                horizontalLineTo(5.0F)
                verticalLineTo(8.0F)
                horizontalLineTo(12.63F)
                lineTo(14.08F, 4.61F)
                close()
            }
        }
        return _notEqualVariant!!
    }

private var _notEqualVariant: ImageVector? = null