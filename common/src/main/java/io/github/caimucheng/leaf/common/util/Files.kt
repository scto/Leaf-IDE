package io.github.caimucheng.leaf.common.util

import com.topjohnwu.superuser.Shell
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
            Shell.cmd("rm", file.absolutePath)
        } else {
            Shell.cmd("rm", "-r", file.absolutePath)
        }.exec().isSuccess
    }

}