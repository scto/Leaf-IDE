package io.github.caimucheng.leaf.common.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.Symbol: ImageVector
    get() {
        if (_symbol != null) {
            return _symbol!!
        }
        _symbol = materialIcon(name = "symbol") {
            materialPath {
                moveTo(2.0F, 7.0F)
                verticalLineTo(14.0F)
                horizontalLineTo(4.0F)
                verticalLineTo(7.0F)
                horizontalLineTo(2.0F)
                moveTo(6.0F, 7.0F)
                verticalLineTo(9.0F)
                horizontalLineTo(10.0F)
                verticalLineTo(11.0F)
                horizontalLineTo(8.0F)
                verticalLineTo(14.0F)
                horizontalLineTo(10.0F)
                verticalLineTo(13.0F)
                curveTo(11.11F, 13.0F, 12.0F, 12.11F, 12.0F, 11.0F)
                verticalLineTo(9.0F)
                curveTo(12.0F, 7.89F, 11.11F, 7.0F, 10.0F, 7.0F)
                horizontalLineTo(6.0F)
                moveTo(15.8F, 7.0F)
                lineTo(15.6F, 9.0F)
                horizontalLineTo(14.0F)
                verticalLineTo(11.0F)
                horizontalLineTo(15.4F)
                lineTo(15.2F, 13.0F)
                horizontalLineTo(14.0F)
                verticalLineTo(15.0F)
                horizontalLineTo(15.0F)
                lineTo(14.8F, 17.0F)
                horizontalLineTo(16.8F)
                lineTo(17.0F, 15.0F)
                horizontalLineTo(18.4F)
                lineTo(18.2F, 17.0F)
                horizontalLineTo(20.2F)
                lineTo(20.4F, 15.0F)
                horizontalLineTo(22.0F)
                verticalLineTo(13.0F)
                horizontalLineTo(20.6F)
                lineTo(20.8F, 11.0F)
                horizontalLineTo(22.0F)
                verticalLineTo(9.0F)
                horizontalLineTo(21.0F)
                lineTo(21.2F, 7.0F)
                horizontalLineTo(19.2F)
                lineTo(19.0F, 9.0F)
                horizontalLineTo(17.6F)
                lineTo(17.8F, 7.0F)
                horizontalLineTo(15.8F)
                moveTo(17.4F, 11.0F)
                horizontalLineTo(18.8F)
                lineTo(18.6F, 13.0F)
                horizontalLineTo(17.2F)
                lineTo(17.4F, 11.0F)
                moveTo(2.0F, 15.0F)
                verticalLineTo(17.0F)
                horizontalLineTo(4.0F)
                verticalLineTo(15.0F)
                horizontalLineTo(2.0F)
                moveTo(8.0F, 15.0F)
                verticalLineTo(17.0F)
                horizontalLineTo(10.0F)
                verticalLineTo(15.0F)
                horizontalLineTo(8.0F)
                close()
            }
        }
        return _symbol!!
    }

private var _symbol: ImageVector? = null