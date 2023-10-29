package io.github.caimucheng.leaf.common.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.ArrowExpandAll: ImageVector
    get() {
        if (_arrowExpandAll != null) {
            return _arrowExpandAll!!
        }
        _arrowExpandAll = materialIcon(
            name = "ArrowExpandAll"
        ) {
            materialPath {
                moveTo(9.5F, 13.09F)
                lineTo(10.91F, 14.5F)
                lineTo(6.41F, 19.0F)
                horizontalLineTo(10.0F)
                verticalLineTo(21.0F)
                horizontalLineTo(3.0F)
                verticalLineTo(14.0F)
                horizontalLineTo(5.0F)
                verticalLineTo(17.59F)
                lineTo(9.5F, 13.09F)
                moveTo(10.91F, 9.5F)
                lineTo(9.5F, 10.91F)
                lineTo(5.0F, 6.41F)
                verticalLineTo(10.0F)
                horizontalLineTo(3.0F)
                verticalLineTo(3.0F)
                horizontalLineTo(10.0F)
                verticalLineTo(5.0F)
                horizontalLineTo(6.41F)
                lineTo(10.91F, 9.5F)
                moveTo(14.5F, 13.09F)
                lineTo(19.0F, 17.59F)
                verticalLineTo(14.0F)
                horizontalLineTo(21.0F)
                verticalLineTo(21.0F)
                horizontalLineTo(14.0F)
                verticalLineTo(19.0F)
                horizontalLineTo(17.59F)
                lineTo(13.09F, 14.5F)
                lineTo(14.5F, 13.09F)
                moveTo(13.09F, 9.5F)
                lineTo(17.59F, 5.0F)
                horizontalLineTo(14.0F)
                verticalLineTo(3.0F)
                horizontalLineTo(21.0F)
                verticalLineTo(10.0F)
                horizontalLineTo(19.0F)
                verticalLineTo(6.41F)
                lineTo(14.5F, 10.91F)
                lineTo(13.09F, 9.5F)
                close()
            }
        }
        return _arrowExpandAll!!
    }

private var _arrowExpandAll: ImageVector? = null