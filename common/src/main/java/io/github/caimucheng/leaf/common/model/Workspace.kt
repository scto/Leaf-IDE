package io.github.caimucheng.leaf.common.model

import java.io.InputStream
import java.io.OutputStream
import java.util.Properties

data class Workspace(
    var name: String,
    var description: String,
    var plugin: String,
    var extraData: Map<String, String>
) {

    companion object {
        const val NAME = "name"

        const val DESCRIPTION = "description"

        const val PLUGIN = "plugin"

        fun loadFromXML(inputStream: InputStream): Workspace? {
            val properties = Properties()
            try {
                inputStream.use {
                    properties.loadFromXML(inputStream)
                }
            } catch (e: Exception) {
                return null
            }

            val propertyNames = properties.stringPropertyNames()
            var name: String? = null
            var description: String? = null
            var plugin: String? = null
            val extraData = HashMap<String, String>()
            for (propertyName in propertyNames) {
                when (propertyName) {
                    NAME -> name = properties.getProperty(propertyName)
                    DESCRIPTION -> description = properties.getProperty(propertyName, "")
                    PLUGIN -> plugin = properties.getProperty(propertyName)
                    else -> extraData[propertyName] = properties.getProperty(propertyName)
                }
            }

            if (name == null || description == null || plugin == null) {
                return null
            }

            return Workspace(
                name, description, plugin, extraData
            )
        }

    }

    fun storeToXML(outputStream: OutputStream) {
        val properties = Properties()
        properties.setProperty(NAME, name)
        properties.setProperty(DESCRIPTION, description)
        properties.setProperty(PLUGIN, plugin)

        val propertyNames = extraData.keys
        for (name in propertyNames) {
            properties.setProperty(name, extraData[name])
        }
        outputStream.use {
            properties.storeToXML(outputStream, null, "UTF-8")
        }
    }

}