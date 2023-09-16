@file:Suppress("UnstableApiUsage")

include(":app")
include(":common")
include(":plugin-api")
include(":plugin-nodejs")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://androidx.dev/storage/compose-compiler/repository") }
    }
}

rootProject.name = "Leaf IDE"