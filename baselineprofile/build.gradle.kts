import java.io.FileInputStream
import java.util.Properties
import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("com.android.test")
//    id("androidx.baselineprofile")
    id("org.jetbrains.kotlin.android")
}

val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(rootProject.file("keystore.properties")))

android {
    namespace = "com.protsprog.baselineprofile"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        minSdk = 28 //android 9
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // We use a bundled debug keystore, to allow debug builds from CI to be upgradable
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        create("release") {
            storeFile = rootProject.file(keystoreProperties["RELEASE_STORE_FILE"] as String)
            storePassword = keystoreProperties["RELEASE_STORE_PASSWORD"] as String
            keyAlias = keystoreProperties["RELEASE_KEY_ALIAS"] as String
            keyPassword = keystoreProperties["RELEASE_KEY_PASSWORD"] as String
        }

    }


    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("baselineprofile") {
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            matchingFallbacks += listOf("debug")
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("androidx.profileinstaller:profileinstaller:1.3.0")
//    "baselineProfile"(project(mapOf("path" to ":baselineprofile")))
    implementation("androidx.benchmark:benchmark-macro-junit4:1.2.2")
    implementation("androidx.test.ext:junit:1.1.5")

    implementation("androidx.test:runner:1.5.0")
//    implementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "baselineprofile"
    }
}

android {
    testOptions {
        managedDevices {
            devices {
                create ("pixel2Api31", ManagedVirtualDevice::class) {
                    device = "Pixel 2"
                    apiLevel = 31
                    systemImageSource = "aosp" // TODO: use aosp-atd when b/228946895 is fixed
                }
            }
        }
    }
}