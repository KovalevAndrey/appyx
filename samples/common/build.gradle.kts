import org.jetbrains.kotlin.config.JvmTarget

plugins {
    id("com.android.library")
    alias(libs.plugins.compose.compiler)
    id("kotlin-android")
    id("kotlin-parcelize")
    id("appyx-lint")
    id("appyx-detekt")
}

android {
    namespace = "com.bumble.appyx.samples.common"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JvmTarget.JVM_11.toString()
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)

    api(composeBom)
    api(project(":libraries:core"))
    api(libs.compose.ui.ui)

    implementation(composeBom)
    implementation(libs.androidx.lifecycle.java8)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.tooling)
    implementation(libs.coil.compose)
}
