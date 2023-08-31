package leaf.plugin.nodejs

import io.github.caimucheng.leaf.plugin.PluginProject

class NodeJSPluginProject : PluginProject() {

    override fun getDisplayedPictureResId(): Int {
        return R.mipmap.displayed_nodejs_picture
    }

    override fun getDisplayedTitleId(): Int {
        return R.string.displayed_nodejs_title
    }

}