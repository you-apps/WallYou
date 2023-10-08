plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.google.android.renderscript"
    compileSdk = 33
    buildToolsVersion = "31.0.0"

    defaultConfig {
        minSdk = 16
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")
    implementation("androidx.core:core-ktx:1.9.0")
}
