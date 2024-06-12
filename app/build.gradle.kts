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
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
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
    implementation("androidx.core:core-ktx:1.13.1")
    //compose core
    val composeLibsVersion = "1.6.7"
    val composeUiLibsVersion = "1.6.8"
    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiLibsVersion")
    implementation("androidx.compose.ui:ui:$composeUiLibsVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiLibsVersion")
    implementation("androidx.activity:activity-compose:1.9.0")
    //compose material3
    implementation("androidx.compose.material:material:$composeLibsVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeLibsVersion")
    //material3
    val material3Version = "1.2.1"
    implementation("androidx.compose.material3:material3:$material3Version")
    implementation("androidx.compose.material3:material3-window-size-class:$material3Version")
    //compose navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}