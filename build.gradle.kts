import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
plugins {
    id("com.android.application") version "8.2.1" apply false
//    or: https://developer.android.com/studio/projects/android-library
    id("com.android.library") version "8.2.1" apply false

    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
//https://kotlinlang.org/docs/kapt.html
    kotlin("kapt") version "1.8.20" apply false
    kotlin("plugin.serialization") version "1.8.20" apply false
//https://github.com/google/ksp/releases
    id("com.google.devtools.ksp") version "1.8.20-1.0.11" apply false
//https://plugins.gradle.org/plugin/com.diffplug.spotless
    id("com.diffplug.spotless") version "6.19.0"

    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false

//    id("com.google.gms.google-services") version "4.4.0" apply false
//    id("com.android.test") version "8.2.1" apply false
//    id("org.jetbrains.kotlin.plugin.parcelize") version "1.8.20" apply false
//    id("androidx.baselineprofile")
}
//https://docs.gradle.org/current/userguide/kotlin_dsl.html#extra_properties
ext {
    extra["appCompatVersion"] = "1.6.1"
    extra["hiltVersion"] = "2.46.1"
    extra["roomVersion"] = "2.6.1"
    extra["navVersion"] = "2.5.3"
    extra["lifecycleVersion"] = "2.6.1"
    extra["glideVersion"] = "4.14.2"
    extra["bomVersion"] = "2023.06.01"
    extra["activityComposeVersion"] = "1.7.2"
    extra["retrofitVersion"] = "2.9.0"
    extra["mochiVersion"] = "1.14.0"
    extra["coilVersion"] = "2.4.0"
    extra["accompanistVersion"] = "0.30.1"
    extra["cameraxVersion"] = "1.2.2"
}

//subprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
//             Use experimental APIs
//        freeCompilerArgs = listOf("-progressive", "-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs += "-progressive"
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            freeCompilerArgs += "-opt-in=kotlin.Experimental"
//            jvmTarget = "17"
            jvmTarget = JavaVersion.VERSION_17.toString()
//        freeCompilerArgs = listOf("-progressive",
            //        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
        }
    }
//}
