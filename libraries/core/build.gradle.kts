import org.jetbrains.kotlin.config.JvmTarget

plugins {
    id("com.android.library")
    alias(libs.plugins.compose.compiler)
    id("kotlin-android")
    id("kotlin-parcelize")
    id("appyx-publish-android")
    id("appyx-lint")
    id("appyx-detekt")
}

android {
    namespace = "com.bumble.appyx.core"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JvmTarget.JVM_11.toString()
    }
    testOptions {
        unitTests.all {
            // interface method default implementation
            it.exclude("**/*\$DefaultImpls.class")
        }
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)

    api(composeBom)
    api(project(":libraries:customisations"))
    api(libs.androidx.lifecycle.common)
    api(libs.compose.animation.core)
    api(libs.compose.animation.android)
    api(libs.compose.runtime)
    api(libs.androidx.appcompat)
    api(libs.kotlin.coroutines.android)

    implementation(composeBom)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.java8)
    implementation(libs.compose.foundation.layout)


    testImplementation(project(":libraries:testing-junit4"))
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)

    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.compose.foundation)
    androidTestImplementation(project(":libraries:testing-ui"))
}
