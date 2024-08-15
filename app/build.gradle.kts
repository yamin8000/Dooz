/*
 *     Dooz
 *     build.gradle Created by Yamin Siahmargooei at 2022/3/31
 *     This file is part of Dooz.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Dooz is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Dooz is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Dooz.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.plugin)
    alias(libs.plugins.kotlin.parcelize)
}

private val appId = "io.github.yamin8000.dooz"

android {
    namespace = appId
    compileSdk = 34

    defaultConfig {
        applicationId = appId
        minSdk = 24
        targetSdk = 34
        versionCode = 18
        versionName = "1.0.18"
        vectorDrawables.useSupportLibrary = true
        base.archivesName = "$applicationId-v$versionCode-n$versionName"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = true
            isShrinkResources = true
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //core android
    implementation(libs.androidx.core.ktx)
    //compose core
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)
    //material3
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size)
    //compose navigation
    implementation(libs.androidx.navigation.compose)
    //datastore
    implementation(libs.androidx.datastore.preferences)
}