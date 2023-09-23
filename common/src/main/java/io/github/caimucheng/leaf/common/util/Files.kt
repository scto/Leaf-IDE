package io.github.caimucheng.leaf.common.util

import java.io.File

object Files {

    fun delete(path: String): Boolean {
        return delete(File(path))
    }

    fun delete(file: File): Boolean {
        if (!file.exists()) {
            return false
        }

        return if (file.isFile) {
            file.delete()
        } else {
            var isSucessful = true
            for (child in (file.listFiles() ?: emptyArray())) {
                if (!isSucessful) {
                    delete(child)
                } else {
                    isSucessful = delete(child)
                }
            }
            if (isSucessful) {
                isSucessful = file.delete()
            } else {
                file.delete()
            }
            isSucessful
        }
    }

    fun rename(fromPath: String, toPath: String): Boolean {
        return rename(File(fromPath), File(toPath))
    }

    fun rename(fromFile: File, toFile: File): Boolean {
        if (!fromFile.exists()) {
            return false
        }

        return fromFile.renameTo(toFile)
    }

}