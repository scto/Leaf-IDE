
import com.android.build.gradle.BaseExtension
import java.io.FileInputStream
import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version Versions.AndroidGradlePluginVersion apply false
    id("org.jetbrains.kotlin.android") version Versions.KotlinVersion apply false
    id("com.android.library") version Versions.AndroidGradlePluginVersion apply false
}

fun Project.configureBaseExtension() {
    extensions.findByType(BaseExtension::class.java)?.run {
        compileSdkVersion(Versions.CompileSdkVersion)

        defaultConfig {
            minSdk = Versions.MinSdkVersion
            //noinspection ExpiredTargetSdkVersion
            targetSdk = Versions.TargetSdkVersion
            versionCode = Versions.VersionCode
            versionName = Versions.VersionName
        }

        compileOptions {
            sourceCompatibility = Versions.CompilingJavaVersion
            targetCompatibility = Versions.CompilingJavaVersion
        }

        signingConfigs {
            create("shared") {
                var extStoreFile = rootProject.file("buildKey.jks")
                var extStorePassword: String? = System.getenv("KEYSTORE_PASSWORD")
                var extKeyAlias: String? = System.getenv("KEY_ALIAS")
                var extKeyPassword: String? = System.getenv("KEY_PASSWORD")
                val extIsNativeEnvironment =
                    extStorePassword == null || extKeyAlias == null || extKeyPassword == null
                val extKeystoreProperties = Properties()
                if (extIsNativeEnvironment) {
                    val keystorePropertiesFile = rootProject.file("keystore.properties")
                    if (keystorePropertiesFile.exists() && keystorePropertiesFile.isFile) {
                        runCatching {
                            FileInputStream(keystorePropertiesFile).use {
                                extKeystoreProperties.load(it)
                            }
                            extStorePassword = extKeystoreProperties.getProperty("storePassword")
                            extKeyAlias = extKeystoreProperties.getProperty("keyAlias")
                            extKeyPassword = extKeystoreProperties.getProperty("keyPassword")
                        }.onFailure {
                            extStoreFile = rootProject.file("fallback.jks")
                            extStorePassword = "123456"
                            extKeyAlias = "fallback"
                            extKeyPassword = "123456"
                        }
                    } else {
                        extStoreFile = rootProject.file("fallback.jks")
                        extStorePassword = "123456"
                        extKeyAlias = "fallback"
                        extKeyPassword = "123456"
                    }
                }

                storeFile = extStoreFile
                storePassword = extStorePassword
                keyAlias = extKeyAlias
                keyPassword = extKeyPassword
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
        }

        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
                signingConfig = signingConfigs.getByName("shared")
            }
            getByName("release") {
                isMinifyEnabled = false
                signingConfig = signingConfigs.getByName("shared")
            }
        }
    }
}

subprojects {
    plugins.withId("com.android.application") {
        configureBaseExtension()
    }
    plugins.withId("com.android.library") {
        configureBaseExtension()
    }
}