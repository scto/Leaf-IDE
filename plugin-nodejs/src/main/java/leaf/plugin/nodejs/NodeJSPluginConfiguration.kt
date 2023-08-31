package leaf.plugin.nodejs

import io.github.caimucheng.leaf.plugin.PluginConfiguration

class NodeJSPluginConfiguration : PluginConfiguration() {

    override fun enabled(): Boolean {
        return true
    }

    override fun author(): String {
        return "SuMuCheng"
    }

}