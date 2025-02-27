plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.youllbecold.trustme"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.youllbecold.trustme"
        minSdk = 29
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Project modules
    api(project(":recomendation"))
    api(project(":logdatabase"))
    api(project(":weather"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // Immutable collections
    implementation(libs.kotlin.collections.immutable)

    // Navigation component
    implementation(libs.androidx.navigation.runtime.ktx)

    // Data store
    implementation(libs.androidx.datastore.preferences)

    // Koin
    implementation(libs.koin)
    implementation(libs.koin.annotations)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson.converter)
    implementation(libs.okhttp3.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines)

    // Google services for location
    implementation(libs.play.services.location)

    // For location permission flow
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.swiperefresh)

    implementation(libs.foundation)

    // DB
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Work Manager
    implementation(libs.androidx.work.runtime.ktx)

    // Reflection
    implementation(libs.kotlin.reflect)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}