package io.github.caimucheng.leaf.common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.Magnify: ImageVector
    get() {
        if (_magnify != null) {
            return _magnify!!
        }
        _magnify = materialIcon(
            name = "Magnify"
        ) {
            materialPath {
                moveTo(9.5F, 3.0F)
                arcTo(6.5F, 6.5F, 0.0F, false, true, 16.0F, 9.5F)
                curveTo(16.0F, 11.11F, 15.41F, 12.59F, 14.44F, 13.73F)
                lineTo(14.71F, 14.0F)
                horizontalLineTo(15.5F)
                lineTo(20.5F, 19.0F)
                lineTo(19.0F, 20.5F)
                lineTo(14.0F, 15.5F)
                verticalLineTo(14.71F)
                lineTo(13.73F, 14.44F)
                curveTo(12.59F, 15.41F, 11.11F, 16.0F, 9.5F, 16.0F)
                arcTo(6.5F, 6.5F, 0.0F, false, true, 3.0F, 9.5F)
                arcTo(6.5F, 6.5F, 0.0F, false, true, 9.5F, 3.0F)
                moveTo(9.5F, 5.0F)
                curveTo(7.0F, 5.0F, 5.0F, 7.0F, 5.0F, 9.5F)
                curveTo(5.0F, 12.0F, 7.0F, 14.0F, 9.5F, 14.0F)
                curveTo(12.0F, 14.0F, 14.0F, 12.0F, 14.0F, 9.5F)
                curveTo(14.0F, 7.0F, 12.0F, 5.0F, 9.5F, 5.0F)
                close()
            }
        }
        return _magnify!!
    }

private var _magnify: ImageVector? = null