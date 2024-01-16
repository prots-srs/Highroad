package com.protsprog.baselineprofile

/*
TO READ

https://developer.android.com/codelabs/android-baseline-profiles-improve#4

https://developer.android.com/topic/performance/baselineprofiles/create-baselineprofile#create-new-profile

https://levelup.gitconnected.com/baseline-profiles-github-actions-ce8bc6858bb3

https://habr.com/ru/companies/dododev/articles/739064/

 */
import android.os.Build
import androidx.annotation.RequiresApi
//import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {
    @RequiresApi(Build.VERSION_CODES.P)
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collect(
        packageName = "com.example.app",
        profileBlock = {
            startActivityAndWait()
        }
    )
}