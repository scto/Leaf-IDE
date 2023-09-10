import org.gradle.api.JavaVersion

object Versions {
    // Project configuration versions
    const val CompileSdkVersion = 34
    const val TargetSdkVersion = 28
    const val MinSdkVersion = 21
    const val VersionCode = 1
    const val VersionName = "1.0.0.0"

    // Project option versions
    val CompilingJavaVersion = JavaVersion.VERSION_11
    const val JvmTarget = "11"
    const val KotlinCompilerExtensionVersion = "1.4.3"
}