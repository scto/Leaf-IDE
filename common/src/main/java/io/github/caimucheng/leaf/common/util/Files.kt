package io.github.caimucheng.leaf.common.util

import java.io.File
import java.math.BigDecimal

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

    fun getFileTotalCount(path: String): Int {
        return getFileTotalCount(File(path))
    }

    fun getFileTotalCount(file: File): Int {
        val children = file.listFiles() ?: emptyArray()
        var count = 0
        for (child in children) {
            if (child.isFile) {
                ++count
            } else {
                // Add itself
                ++count

                // Add its children
                count += getFileTotalCount(child)
            }
        }
        return count
    }

    fun getFileCount(path: String): Int {
        return getFileCount(File(path))
    }

    fun getFileCount(file: File): Int {
        val children = file.listFiles() ?: emptyArray()
        var count = 0
        for (child in children) {
            if (child.isFile) {
                ++count
            } else {
                // Add its children
                count += getFileCount(child)
            }
        }
        return count
    }

    fun getFolderCount(path: String): Int {
        return getFolderCount(File(path))
    }

    fun getFolderCount(file: File): Int {
        val children = file.listFiles() ?: emptyArray()
        var count = 0
        for (child in children) {
            if (child.isDirectory) {
                ++count
            } else {
                // Add its children
                count += getFolderCount(child)
            }
        }
        return count
    }

    fun getTotalBytes(path: String): Long {
        return getTotalBytes(File(path))
    }

    fun getTotalBytes(file: File): Long {
        if (!file.exists()) {
            return 0L
        }

        return if (file.isFile) {
            file.length()
        } else {
            var bytes = 0L
            val children = file.listFiles() ?: emptyArray()
            for (child in children) {
                bytes += if (child.isFile) {
                    child.length()
                } else {
                    getTotalBytes(child)
                }
            }
            bytes
        }
    }

    fun formatBytes(size: Long): String {
        val kb = 1024L
        val mb = kb * 1024
        val gb = mb * 1024

        return if (size >= gb) {
            div(size.toString(), gb.toString()) + " GB"
        } else if (size >= mb) {
            div(size.toString(), mb.toString()) + " MB"
        } else if (size > kb) {
            div(size.toString(), kb.toString()) + " KB"
        } else {
            div(size.toString(), "1") + " Bytes"
        }
    }

    @Suppress("DEPRECATION")
    private fun div(v1: String?, v2: String?): String {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        val divide: BigDecimal = b1.divide(b2, 2, BigDecimal.ROUND_HALF_DOWN)
        return divide.toString()
    }

}