plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.planteria"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.planteria"
        minSdk = 26
        targetSdk = 34
        versionCode = 9
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        debug{
            buildConfigField ("String", "API_BASE_URL", "\"https://perenual.com\"")

        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.google.firebase:firebase-auth:22.3.0")
//    implementation("androidx.camera:camera-view:1.3.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.code.gson:gson:2.9.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.1")
    implementation ("com.github.IslamKhSh:CardSlider:1.0.1")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
//    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    implementation ("com.github.orbitalsonic:Water-Wave-Animation-Android:1.0.0")
    implementation ("com.airbnb.android:lottie:6.2.0")
//    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")
    implementation ("com.google.ar.sceneform:core:1.17.1")
    implementation ("com.google.ar:core:1.41.0")
//    implementation ("io.github.sceneview:arsceneview:2.0.2")
    implementation ("com.google.android.play:app-update:2.1.0")
}