package io.github.caimucheng.leaf.common.util

import com.topjohnwu.superuser.Shell
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
object FileSystem {

    fun mkdirs(path: String): Boolean {
        return mkdirs(File(path))
    }

    fun mkdirs(file: File): Boolean {
        return if (isRootLaunchMode) {
            Shell.cmd("mkdir -p ${file.absolutePath}").exec().isSuccess
        } else {
            file.mkdirs()
        }
    }

    fun exists(path: String): Boolean {
        return exists(File(path))
    }

    fun exists(file: File): Boolean {
        return if (isRootLaunchMode) {
            Shell.cmd("find ${file.absolutePath}").exec().isSuccess
        } else {
            file.exists()
        }
    }

    fun isFile(file: File): Boolean {
        return !isDirectory(file)
    }

    fun isDirectory(file: File): Boolean {
        return if (isRootLaunchMode) {
            val result = Shell.cmd("find ${file.absolutePath} -maxdepth 0 -type d").exec()
            result.isSuccess && result.out.isNotEmpty()
        } else {
            file.isDirectory
        }
    }

}