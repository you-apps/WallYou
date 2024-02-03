plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.bnyro.wallpaper"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.bnyro.wallpaper"
        minSdk = 21
        targetSdk = 33
        versionCode = 21
        versionName = "6.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] += "$projectDir/schemas"
            }
        }

        buildTypes {
            release {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            debug {
                isDebuggable = true
                applicationIdSuffix = ".debug"
            }
        }
        compileOptions {
            isCoreLibraryDesugaringEnabled = true

            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.4.2"
        }
        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}

dependencies {
    val compose_version: String by rootProject.extra
    // Core
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.documentfile:documentfile:1.0.1")

    // UI
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Design libraries
    implementation("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation("androidx.compose.material:material-icons-core:1.4.3")
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    implementation("androidx.palette:palette:1.0.0")


    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Renderscript
    implementation(project(":renderscript-toolkit"))

    // Room
    implementation("androidx.room:room-runtime:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")

    // Local image parsing
    implementation("androidx.exifinterface:exifinterface:1.3.6")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")

    // Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
}
