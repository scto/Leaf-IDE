import org.gradle.api.JavaVersion

object Versions {
    // Project configuration versions
    const val COMPILE_SDK_VERSION = 34
    const val TARGET_SDK_VERSION = 28
    const val MIN_SDK_VERSION = 21
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0.0"

    // Project option versions
    val COMPILING_JAVA_VERSION = JavaVersion.VERSION_11
    const val JVM_TARGET_VERSION = "11"
    const val KOTLIN_COMPILER_EXTENSION_VERSION = "1.4.3"
}