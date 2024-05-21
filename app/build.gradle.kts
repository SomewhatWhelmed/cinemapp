import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.cinemapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cinemapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        val apiKey = properties.getProperty("TMDB_API_KEY") ?: ""
        val urlBase = properties.getProperty("URL_BASE") ?: ""
        val urlBaseImage = properties.getProperty("URL_BASE_IMAGE") ?: ""

        buildFeatures {
            buildConfig = true
            viewBinding = true
        }

        buildConfigField(type = "String", name = "TMDB_API_KEY", value = apiKey)
        buildConfigField(type = "String", name = "URL_BASE", value = urlBase)
        buildConfigField(type = "String", name = "URL_BASE_IMAGE", value = urlBaseImage)


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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.material)
    implementation(libs.paging)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.navigation)
    implementation(libs.navigation.ui)
    implementation(libs.glide)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}