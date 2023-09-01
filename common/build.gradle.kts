plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "io.github.caimucheng.leaf.common"

    kotlinOptions {
        jvmTarget = Versions.JvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.KotlinCompilerExtensionVersion
    }
}

dependencies {
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