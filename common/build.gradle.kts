plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.plugin)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "io.github.yamin8000.dooz.common"
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
    //core android
    api(libs.androidx.core.ktx)
    //compose
    api(libs.androidx.activity.compose)
    api(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.graphics)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.material3)
    api(libs.compose.material.icons.extended)
    androidTestImplementation(platform(libs.compose.bom))
    debugApi(libs.compose.ui.tooling)
    debugApi(libs.compose.ui.test.manifest)
    api(libs.navigation.compose)
    //material
    api(libs.material3)
    api(libs.compose.material3.window.size)
    //hilt
    api(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    api(libs.hilt.lifecycle.compose)
}