package com.protsprog.highroad

/*
TO READ
https://developer.android.com/kotlin/coroutines/test#testdispatchers

https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test#using-in-your-project

https://medium.com/androiddevelopers/cancellation-in-coroutines-aa6b90163629
 */
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CoroutinesTest {

    suspend fun fetchData(): String {
        delay(1000L)
        return "Hello world"
    }

    @Test
    fun dataShouldBeHelloWorld() = runTest {
        val data = fetchData()
        assertEquals("Hello world", data)
    }
}