import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
plugins {
    id("com.android.application") version "8.0.2" apply false
//    or: https://developer.android.com/studio/projects/android-library
    id("com.android.library") version "8.0.2" apply false

    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
//https://kotlinlang.org/docs/kapt.html
    kotlin("kapt") version "1.8.20" apply false
//https://github.com/google/ksp/releases
    id("com.google.devtools.ksp") version "1.8.20-1.0.11" apply false
//https://plugins.gradle.org/plugin/com.diffplug.spotless
    id("com.diffplug.spotless") version "6.19.0"

    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}
//https://docs.gradle.org/current/userguide/kotlin_dsl.html#extra_properties
ext {
    extra["appCompatVersion"] = "1.6.1"
    extra["hiltVersion"] = "2.46.1"
    extra["roomVersion"] = "2.5.0"
    extra["navVersion"] = "2.5.3"
    extra["lifecycleVersion"] = "2.6.1"
    extra["glideVersion"] = "4.14.2"
    extra["bomVersion"] = "2023.06.01"
    extra["activityComposeVersion"] = "1.7.2"
    extra["retrofitVersion"] = "2.9.0"
    extra["mochiVersion"] = "1.14.0"
    extra["coilVersion"] = "2.4.0"
    extra["accompanistVersion"] = "0.30.1"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
//             Use experimental APIs
        freeCompilerArgs = listOf("-progressive", "-opt-in=kotlin.RequiresOptIn")
    }
}

//    apply(plugin = "com.diffplug.spotless")
//    spotless {
//        kotlin {
//            target = "**/*.kt"
//            targetExclude("$buildDir/**/*.kt")
//            targetExclude("bin/**/*.kt")
//            ktlint("0.45.2")
//            licenseHeaderFile = rootProject.file("spotless/copyright.kt")
//        }
//    }
// androidx.test and hilt are forcing JUnit, 4.12. This forces them to use 4.13
//    configurations.configureEach {
//        resolutionStrategy {
//            force("junit:junit:4.13.2")
//        }
//    }
//}
