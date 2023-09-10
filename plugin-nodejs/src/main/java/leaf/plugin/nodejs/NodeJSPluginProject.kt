package leaf.plugin.nodejs

import io.github.caimucheng.leaf.plugin.PluginProject

class NodeJSPluginProject : PluginProject() {

    override fun getDisplayedPictureResId(): Int {
        return R.mipmap.displayed_nodejs_picture
    }

    override fun getDisplayedTitleId(): Int {
        return R.string.displayed_nodejs_title
    }

    override fun getDisplayedProjectLogoResId(): Int {
        return R.drawable.nodejs_logo
    }

    override fun getDisplayedProjectTitleId(): Int {
        return R.string.displayed_project_title
    }

}