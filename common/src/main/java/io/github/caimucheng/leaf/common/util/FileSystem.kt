package io.github.caimucheng.leaf.common.util

import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
object FileSystem {

    fun mkdirs(path: String): Boolean {
        return mkdirs(File(path))
    }

    fun mkdirs(file: File): Boolean {
        return if (isRootLaunchMode) {
            try {
                sudo("mkdir -p ${file.absolutePath}").waitFor() == 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            file.mkdirs()
        }
    }

    fun exists(path: String): Boolean {
        return exists(File(path))
    }

    fun exists(file: File): Boolean {
        return if (isRootLaunchMode) {
            try {
                sudo("find ${file.absolutePath}").waitFor() == 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            file.exists()
        }
    }

    private fun sudo(command: String): Process {
        return Runtime.getRuntime().exec("su -c $command")
    }

}