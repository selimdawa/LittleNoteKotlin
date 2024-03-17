plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.flatcode.littlenote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.flatcode.littlenote"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.26"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")           //Shared Preference
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Layout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4")) //Firebase BOM
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")        //Firebase Fire Store
    //Don't Change
    implementation("com.firebaseui:firebase-ui-firestore:4.2.1")        //Firebase Store UI
    //implementation("com.google.firebase:firebase-database-ktx")
    //implementation("com.google.firebase:firebase-storage-ktx")
    //implementation("com.google.firebase:firebase-crashlytics-ktx")
}