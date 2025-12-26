plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.flatcode.littlenote"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.flatcode.littlenote"
        minSdk = 24
        targetSdk = 36
        versionCode = 6
        versionName = "1.30"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("D:\\MyProjects\\Kotlin\\Little Note\\Little Note\\LittleNote.jks")
            storePassword = "00000000"
            keyAlias = "LittleNote"
            keyPassword = "00000000"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.preference.ktx)           //Shared Preference
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Layout
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.cardview)
    //Firebase
    implementation(platform(libs.firebase.bom)) //Firebase BOM
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)        //Firebase Fire Store
    //Don't Change
    implementation(libs.firebase.ui.firestore)        //Firebase Store UI
    //implementation(libs.firebase.database)
    //implementation(libs.firebase.storage)
    //implementation(libs.firebase.crashlytics)
}