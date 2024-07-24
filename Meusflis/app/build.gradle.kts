plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.googleService)
}

android {
    namespace = "com.example.meusflis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.meusflis"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //SHIMMER EFECTO
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //FIREBASE
    implementation("com.google.firebase:firebase-database:21.0.0")

    //IMAGENES
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.makeramen:roundedimageview:2.3.0")

    // OUTLINE TEXT VIEW
    implementation("com.github.iamBedant:OutlineTextView:1.0.5")

    //DEXTER PERMISOS
    implementation("com.karumi:dexter:6.2.3")

    //EXOPLAYER
    implementation("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")
}