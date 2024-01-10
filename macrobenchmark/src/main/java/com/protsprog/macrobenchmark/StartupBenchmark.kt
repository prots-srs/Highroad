package com.protsprog.macrobenchmark

/*
TO READ

https://developer.android.com/reference/kotlin/androidx/benchmark/macro/Metric
https://developer.android.com/reference/androidx/test/uiautomator/UiDevice

https://developer.android.com/jetpack/compose/testing#uiautomator-interop

https://developer.android.com/codelabs/android-macrobenchmark-inspect#9

https://github.com/android/performance-samples/pull/224
https://github.com/android/performance-samples/tree/main/MacrobenchmarkSample

https://developer.android.com/topic/performance/vitals/launch-time
https://developer.android.com/topic/performance/benchmarking/macrobenchmark-metrics
https://developer.android.com/topic/performance/vitals/render

 */
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 */

private const val TARGET_PACKAGE = "com.protsprog.highroad"
private const val COUNT_ITERATION = 1

@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {

//    private val TARGET_PACKAGE: String = InstrumentationRegistry.getTargetContext().getPackageName()

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        iterations = COUNT_ITERATION,
        startupMode = StartupMode.COLD,
        setupBlock = {
            pressHome()
        }
    ) {
        startActivityAndWait()

        //Modifier.testTag()
        val searchCondition = Until.hasObject(By.res("case_collection"))
        val contentList = device.findObject(By.res("case_list"))
//        println("highroad_macrobenchmarck contentList ${contentList}")

        contentList?.let {
            it.wait(searchCondition, 3_000)
        }
    }
}

@RunWith(AndroidJUnit4::class)
class ScrollBenchmarks {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scroll() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        iterations = COUNT_ITERATION,
        metrics = listOf(FrameTimingMetric()),
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome(500)
            startActivityAndWait()
        }
    ) {

        val searchCondition = Until.hasObject(By.res("case_collection"))
        val contentList = device.findObject(By.res("case_list"))
//        println("highroad_macrobenchmarck contentList ${contentList}")
//        println("highroad_macrobenchmarck margin ${device.displayWidth / 5}")

        contentList?.let {
            it.wait(searchCondition, 3_000)
//            it.setGestureMargin(device.displayWidth / 5)
//            it.fling(Direction.DOWN)
            it.scroll(Direction.DOWN, 100f)
        }

/*        if (contentList != null) {
            contentList.wait(searchCondition, 3_000)
//        contentList.setGestureMargin(device.displayWidth / 5)

//        прокрутка you can also use scroll() or swipe()
//        contentList.fling(Direction.DOWN)
            contentList.scroll(Direction.DOWN, 100f)
        }

 */

        device.waitForIdle()
    }
}