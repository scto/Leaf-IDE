plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "leaf.plugin.nodejs"

    defaultConfig {
        applicationId = "leaf.plugin.nodejs"
        versionCode = NodeJSPluginVersions.VersionCode
        versionName = NodeJSPluginVersions.VersionName
    }

    kotlinOptions {
        jvmTarget = Versions.JvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.KotlinCompilerExtensionVersion
    }

    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/DebugProbesKt.bin"
            )
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":plugin-api"))
    implementation("androidx.core:core-splashscreen:${Versions.SplashScreen}")
    implementation("androidx.compose.material:material-icons-extended:${Versions.MaterialIconsExtended}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.ConstraintLayout}")
    implementation("com.github.topjohnwu.libsu:core:${Versions.LibSU}")
    implementation("com.github.topjohnwu.libsu:service:${Versions.LibSU}")
    implementation("com.github.topjohnwu.libsu:nio:${Versions.LibSU}")
    //noinspection GradleDependency
    implementation("androidx.core:core-ktx:${Versions.CoreKtx}")
    implementation("androidx.navigation:navigation-compose:${Versions.Navigation}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LifecycleRuntimeKtx}")
    implementation("androidx.activity:activity-compose:${Versions.ActivityCompose}")
    implementation(platform("androidx.compose:compose-bom:${Versions.ComposeBom}"))
    implementation("com.github.skydoves:cloudy:${Versions.Cloudy}")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
}