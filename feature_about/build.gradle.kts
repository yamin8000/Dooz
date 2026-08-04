plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.plugin)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "io.github.yamin8000.dooz.feature_about"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    //core
    implementation(project(":common"))
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.lifecycle.compose)
}