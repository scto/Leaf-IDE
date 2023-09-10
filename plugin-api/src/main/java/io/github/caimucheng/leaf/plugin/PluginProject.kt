package io.github.caimucheng.leaf.plugin

import android.content.res.Resources

abstract class PluginProject {

    private lateinit var resources: Resources

    open fun setResources(resources: Resources) {
        this.resources = resources
    }

    open fun getResources(): Resources {
        return resources
    }

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

}