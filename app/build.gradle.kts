import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

private val properties = Properties()
    .apply { load(rootProject.file("local.properties").inputStream()) }

android {
    namespace = "io.github.chhabra_dhiraj.spaceflightnews"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.chhabra_dhiraj.spaceflightnews"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    val baseUrl = "BASE_URL"
    val baseUrlValueType = "String"
    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField(
                type = baseUrlValueType,
                name = baseUrl,
                value = properties.getProperty("BASE_URL_DEBUG")
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                type = baseUrlValueType,
                name = baseUrl,
                value = properties.getProperty("BASE_URL_RELEASE")
            )
        }
        create("dummyServer") {
            isMinifyEnabled = false
            buildConfigField(
                type = baseUrlValueType,
                name = baseUrl,
                value = properties.getProperty("BASE_URL_DUMMY_SERVER")
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlinOptions {
        jvmTarget = "19"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Material Design
    implementation(libs.androidx.material3)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation Compose
    implementation(libs.navigation.compose)

    // Dagger Hilt
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    // Kotlinx Serialization
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)

    // Coil Compose
    implementation(libs.coil.compose)

    // Logging with Timber
    implementation(libs.timber)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)

    // Robolectric
    testImplementation(libs.robolectric)
    // Mockk framework
    testImplementation(libs.mockk)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//
//    androidTestImplementation "androidx.test:runner:$androidXTestVersion"
//    androidTestImplementation "androidx.test:rules:$androidXTestVersion"
//    // Optional -- UI testing with Espresso
//    androidTestImplementation "androidx.test.espresso:espresso-core:$espressoVersion"
//    // Optional -- UI testing with UI Automator
//    androidTestImplementation "androidx.test.uiautomator:uiautomator:$uiAutomatorVersion"
//    // Optional -- UI testing with Compose
//    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"

    // Compose Tests
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Compose Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}