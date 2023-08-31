plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "io.github.caimucheng.leaf.ide"
    compileSdk = Versions.CompileSdkVersion

    defaultConfig {
        applicationId = "io.github.caimucheng.leaf.ide"
        minSdk = Versions.MinSdkVersion
        //noinspection ExpiredTargetSdkVersion
        targetSdk = Versions.TargetSdkVersion
        versionCode = Versions.VersionCode
        versionName = Versions.VersionName
    }

    signingConfigs {
        create("shared") {
            storeFile = file("../buildKey.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
            this.enableV1Signing = true
            this.enableV2Signing = true
            this.enableV3Signing = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("shared")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("shared")
        }
    }
    compileOptions {
        sourceCompatibility = Versions.CompilingJavaVersion
        targetCompatibility = Versions.CompilingJavaVersion
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
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":plugin-api"))
    implementation("androidx.core:core-splashscreen:${Versions.SplashScreen}")
    implementation("androidx.compose.material:material-icons-extended:${Versions.MaterialIconsExtended}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.ConstraintLayout}")
    implementation("com.airbnb.android:lottie-compose:${Versions.LottieCompose}")
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

tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}