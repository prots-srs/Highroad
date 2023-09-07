package com.protsprog.highroad.authentication.data

/*
TO READ

https://developer.android.com/reference/android/os/Build

https://developer.android.com/codelabs/advanced-kotlin-coroutines?hl=en#0

https://github.com/android/codelab-kotlin-coroutines
 */
import android.util.Log
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

sealed interface AuthResponseResource<out T> {
    data class Success<T>(val data: T) : AuthResponseResource<T>
    data class Error(val error: String = "") : AuthResponseResource<Nothing>

    data class Loading(val status: Boolean) : AuthResponseResource<Nothing>
}

@Immutable
data class UserModel(
    val email: String,
    val name: String,
)

class AuthRepository @Inject constructor() {

    private val deviceName: String = android.os.Build.FINGERPRINT

    fun requestLogin(
        email: String, password: String
    ): Flow<AuthResponseResource<String>> = flow {
        delay(1000)

        if (passwdBackEnd.containsKey(email)) {
            if (passwdBackEnd[email] == password) {
                emit(AuthResponseResource.Success(tokenByEmailBackEnd[email]!!))
            } else {
                emit(AuthResponseResource.Error("Password wrong"))
            }
        } else {
            emit(AuthResponseResource.Error("Email not found"))
        }
    }.onStart { emit(AuthResponseResource.Loading(true)) }
        .onCompletion { emit(AuthResponseResource.Loading(false)) }
        .catch { error -> emit(AuthResponseResource.Error(error.message.toString())) }

    fun requestUserData(token: String): Flow<AuthResponseResource<UserModel>> = flow {
        delay(1000)
        if (usersByTokenBackEnd.containsKey(token)) {
            emit(AuthResponseResource.Success(usersByTokenBackEnd[token]!!))
        } else {
            emit(AuthResponseResource.Error("Token wrong"))
        }
    }.onStart { emit(AuthResponseResource.Loading(true)) }
        .onCompletion { emit(AuthResponseResource.Loading(false)) }
        .catch { error -> emit(AuthResponseResource.Error(error.message.toString())) }

    fun saveUserData(user: UserModel) {

    }
}

/*
to test
 */
private val emailsBackEnd: List<String> = listOf(
    "testlogin@protsprog.com",
    "wolf@a.com"
)
private val passwdBackEnd: Map<String, String> = mapOf(
    emailsBackEnd[0] to "123",
    emailsBackEnd[1] to "abc"
)

private val tokenByEmailBackEnd: Map<String, String> = mapOf(
    emailsBackEnd[0] to "test0token",
    emailsBackEnd[1] to "wolf0token"
)

private val usersByTokenBackEnd: Map<String, UserModel> = mapOf(
    tokenByEmailBackEnd[emailsBackEnd[0]]!! to UserModel(
        name = "Jone",
        email = emailsBackEnd[0]
    ),
    tokenByEmailBackEnd[emailsBackEnd[1]]!! to UserModel(
        name = "one blood",
        email = emailsBackEnd[1]
    )
)

