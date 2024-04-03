plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.scannerproto"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.scannerproto"
        minSdk = 24
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

    //image classification
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    //custom image classification
    implementation("com.google.mlkit:image-labeling-custom-common:17.0.0")
    //object detection
    implementation("com.google.mlkit:object-detection:17.0.1")

    // CameraX core library using the camera2 implementation
    //def camerax_version ("1.1.0-alpha08")
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation ("androidx.camera:camera-core:${"1.1.0-alpha08"}")
    implementation ("androidx.camera:camera-camera2:${"1.1.0-alpha08"}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:${"1.1.0-alpha08"}")
    // If you want to additionally use the CameraX View class
    implementation ("androidx.camera:camera-view:1.0.0-alpha14")
    // If you want to additionally use the CameraX Extensions library
    implementation ("androidx.camera:camera-extensions:1.0.0-alpha14")
}