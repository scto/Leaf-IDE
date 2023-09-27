@file:Suppress("unused", "unused", "unused", "unused")

package io.github.caimucheng.leaf.plugin

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import java.io.File

@Suppress("unused", "unused")
abstract class PluginProject : PluginBase() {

    /**
     * @return The displayed image resource id
     * */
    abstract fun getDisplayedPictureResId(): Int

    /**
     * @return The displayed title resource id
     * */
    abstract fun getDisplayedTitleId(): Int

    /**
     * @return The displayed project logo resource id
     * */
    abstract fun getDisplayedProjectLogoResId(): Int

    /**
     * @return The displayed project title resource id
     * */
    abstract fun getDisplayedProjectTitleId(): Int

    /**
     * Called when create project dialog
     * */
    @Composable
    abstract fun CreateProjectScreen(back: () -> Unit, backHome: () -> Unit)

    open fun getFileIcon(file: File): ImageVector? {
        return null
    }


}