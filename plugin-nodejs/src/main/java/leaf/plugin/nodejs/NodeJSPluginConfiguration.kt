package leaf.plugin.nodejs

import io.github.caimucheng.leaf.plugin.PluginConfiguration

class NodeJSPluginConfiguration : PluginConfiguration() {

    override fun enabled(): Boolean {
        return true
    }

    override fun descriptionId(): Int {
        return R.string.plugin_description
    }

    override fun author(): String {
        return "SuMuCheng"
    }

}