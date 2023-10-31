package com.protsprog.highroad

import com.protsprog.highroad.authentication.data.AuthService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AuthTest {
    @Test
    fun test_get_login_token() {
//        val scope = CoroutineScope(Dispatchers.Default)
//                GlobalScope.launch {
//        scope.launch {
//        withContext(Dispatchers.IO) {
        runBlocking {
            launch {
                try {
                    val response = AuthService.authService.login(
                        email = "testlogin@protsprog.com",
                        password = "test0token",
                        deviceName = "Serhii`s Moto"
                    )
                    println("request: ${response.code()}")
                    println("request: ${response.body()}")

                    if (response.code() == 200) {
                        response.body()?.let {
                            println("request body: ${it.token}")
                        }
                    }/*
                    val body = response.body()
                    println('-')
                    println("request: ${response}")
                    val hasCookie = response.headers().filter {
                        it.first == "Set-Cookie"
                    }.isNotEmpty()
                    println("request hasCookie: ${hasCookie}")
                    println("request body: ${body?.length.toString()}")

                    if (body != null && !hasCookie && body.length < 100) {
                        println("request body: ${response.body()}")
                    }*/

                    println('-')
                } catch (e: Exception) {
                    println('-')
                    println("error: ${e.message.toString()}")
                    println('-')
                }
            }
        }
    }

    @Test
    fun test_get_user_data() {
        val token = "30|MWwiSQzkYX3WIi8T2DT85iGsPbI0YQOJW4CTItz34a6bd219"
        runBlocking {
            launch {
                try {
                    val response = AuthService.authService.getUser("Bearer ${token}")
                    println("request: ${response.code()}")
                    println("request: ${response.body()}")
                } catch (e: Exception) {
                    println('-')
                    println("error: ${e.message.toString()}")
                    println('-')
                }
            }
        }
    }

    @Test
    fun test_save_user_data() {
        val token = "39|8EZxTTNglypL6Cw8NAYapOZnx1mxFtKMxrEcxlqC512c2e54"

        runBlocking {
            try {
                val deferred = async {
                    AuthService.authService.updateUser(
                        token = "Bearer ${token}", name = "John Wick"
                    )
                }
                val response = deferred.await()
                println("request: ${response.code()}")
                println("request: ${response.body()}")
            } catch (e: Exception) {
                println('-')
                println("error: ${e.message.toString()}")
                println('-')
            }
        }
    }
}