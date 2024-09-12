plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.sabir.meusflis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sabir.meusflis"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.autoimageslider)

    implementation(libs.glide)

    annotationProcessor(libs.glide.compiler)

    implementation(libs.exoplayer)

    implementation ("com.google.firebase:firebase-auth:21.0.0")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.makeramen:roundedimageview:2.3.0")
}