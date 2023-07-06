import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

// Reads the Google maps key that is used in the AndroidManifest
val keystoreProperties = Properties().apply {
    FileInputStream(rootProject.file("keystore.properties"))
}

android {
    compileSdk = 33
    namespace = "com.protsprog.highroad"
    defaultConfig {
        applicationId = "com.protsprog.highroad"
//        testApplicationId = "com.protsprog.highroad.test"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = keystoreProperties["MAPS_API_KEY"].toString()
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    signingConfigs {
// We use a bundled debug keystore, to allow debug builds from CI to be upgradable
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    buildFeatures {
        compose = true
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
        resValues = true
    }
//https://developer.android.com/jetpack/androidx/releases/compose-kotlin
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.5"
    }
//https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    /*
    kotlinOptions {
        jvmTarget = "1.8"
    }

     */
}
kotlin {
    jvmToolchain(17)
}
hilt {
    enableAggregatingTask = true
}
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.0-1.0.9")
// Core Android dependencies
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:${rootProject.ext.get("appCompatVersion")}")
    implementation("androidx.activity:activity-compose:${rootProject.ext.get("activityComposeVersion")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.ext.get("lifecycleVersion")}")

//    https://m2.material.io/develop/android/docs/getting-started
    implementation("com.google.android.material:material:1.9.0")

    // Compose
    //https://developer.android.com/jetpack/compose/bom/bom-mapping
    val composeBom = platform("androidx.compose:compose-bom:${rootProject.ext.get("bomVersion")}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.compiler:compiler:1.4.7")

    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime")
//    implementation("androidx.compose.runtime:runtime-livedata")--
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material:material")
// Tooling
    implementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-tooling")
// Instrumented tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

//    Hilt
//https://dagger.dev/hilt/gradle-setup.html
    implementation("com.google.dagger:hilt-android:${rootProject.ext.get("hiltVersion")}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.ext.get("hiltVersion")}")
//???    kapt("com.google.dagger:hilt-compiler:${rootProject.ext.get("hiltVersion")}")
//----    ksp("com.google.dagger:hilt-compiler:${rootProject.ext.get("hiltVersion")}")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
//    androidTestImplementation("com.google.dagger:hilt-android:${rootProject.ext.get("hiltVersion")}")
//    androidTestImplementation("com.google.dagger:hilt-android-testing:${rootProject.ext.get("hiltVersion")}")

    implementation("androidx.navigation:navigation-compose:${rootProject.ext.get("navVersion")}")
    androidTestImplementation("androidx.navigation:navigation-testing:${rootProject.ext.get("navVersion")}")

// https://developers.google.com/maps/documentation/android-sdk/config
    implementation("com.google.android.gms:play-services-maps:18.1.0")
//    implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
    implementation("com.google.maps.android:maps-v3-ktx:3.4.0")
    constraints {
// Volley is a transitive dependency of maps
        implementation("com.android.volley:volley:1.2.1") {
            because("Only volley 1.2.0 or newer are available on maven.google.com")
        }
    }

//lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.ext.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.ext.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${rootProject.ext.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.ext.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${rootProject.ext.get("lifecycleVersion")}")
//Room
//https://developer.android.com/jetpack/androidx/releases/room#declaring_dependencies
    implementation("androidx.room:room-runtime:${rootProject.ext.get("roomVersion")}")
    annotationProcessor("androidx.room:room-compiler:${rootProject.ext.get("roomVersion")}")
    implementation("androidx.room:room-ktx:${rootProject.ext.get("roomVersion")}")
    ksp("androidx.room:room-compiler:${rootProject.ext.get("roomVersion")}")
    testImplementation("androidx.room:room-testing:${rootProject.ext.get("roomVersion")}")

// Coil
//https://github.com/coil-kt/coil
    implementation("io.coil-kt:coil:${rootProject.ext.get("coilVersion")}")
    implementation("io.coil-kt:coil-compose:${rootProject.ext.get("coilVersion")}")

// Retrofit
    implementation("com.squareup.retrofit2:retrofit:${rootProject.ext.get("retrofitVersion")}")
// Retrofit with Moshi Converter
    implementation("com.squareup.retrofit2:converter-moshi:${rootProject.ext.get("retrofitVersion")}")

// Moshi
//https://github.com/square/moshi#codegen
    implementation("com.squareup.moshi:moshi-kotlin:${rootProject.ext.get("mochiVersion")}")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:${rootProject.ext.get("mochiVersion")}")

//    TEST
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")

    implementation("com.google.accompanist:accompanist-swiperefresh:${rootProject.ext.get("accompanistVersion")}")
    implementation("com.google.accompanist:accompanist-insets:${rootProject.ext.get("accompanistVersion")}")
    implementation("com.google.accompanist:accompanist-systemuicontroller:${rootProject.ext.get("accompanistVersion")}")

//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation( "androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation( "androidx.test.espresso:espresso-contrib:$espressoVersion")
//    androidTestImplementation( "androidx.test.espresso:espresso-intents:$espressoVersion")
//    androidTestImplementation( "androidx.test.espresso:espresso-accessibility:$espressoVersion")
//    androidTestImplementation( "androidx.test.espresso:espresso-web:$espressoVersion")
//    androidTestImplementation( "androidx.test.espresso.idling:idling-concurrent:$espressoVersion")

//    implementation("com.google.accompanist:accompanist-themeadapter-material:0.28.0")
}

//    testOptions.unitTests {
//        includeAndroidResources = true
//    }
//    packagingOptions {
//        resources {
//            excludes += listOf("/META-INF/AL2.0", "/META-INF/LGPL2.1")
//        }
//    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }

//--------------------------------/////////////////////////

//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.3")
//    androidTestImplementation(
//        "androidx.test.espresso:espresso-core:3.4.0"
//    )

//    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
//    testImplementation("androidx.test:core-ktx:1.5.0")
//    testImplementation("org.robolectric:robolectric:4.9.2")

//    implementation("com.github.bumptech.glide:glide:${glide_version}")
//    annotationProcessor("com.github.bumptech.glide:compiler:${glide_version}")

// UI Tests
//    androidTestImplementation(
//        "androidx.compose.ui:ui-test-junit4"
//    )
//    debugImplementation("androidx.compose.ui:ui-test-manifest")

//    androidTestImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test:core:1.5.0")
//    androidTestImplementation("androidx.test:runner:1.5.2")
//    androidTestImplementation("androidx.test:rules:1.5.0")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
//    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

//    kaptAndroidTest("com.google.dagger:hilt-compiler:2.46")