package com.protsprog.highroad.authentication

/*
TO READ
https://developer.android.com/codelabs/android-network-security-config#0

https://developer.android.com/reference/android/os/Build

https://developer.android.com/codelabs/advanced-kotlin-coroutines?hl=en#0

https://github.com/android/codelab-kotlin-coroutines

LOOK FOR
wrapContentSize
 */
import androidx.compose.runtime.Immutable
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

    suspend fun requestLogin(
        email: String, password: String
    ): Flow<AuthResponseResource<String>> = flow {
        try {
            val response = AuthService.authService.login(
                email = email,
                password = password,
                deviceName = deviceName
            )
            val body: AuthTokenResponse = response.body() ?: AuthTokenResponse()
            if (response.code() == 200) {
                if (body.token.isNotEmpty()) {
                    emit(AuthResponseResource.Success(body.token))
                } else {
                    emit(AuthResponseResource.Error(body.error))
                }
            } else {
                emit(AuthResponseResource.Error("Authorized error"))
            }
        } catch (e: Exception) {
            emit(AuthResponseResource.Error("Network error: ${e.message.toString()}"))
        }
    }.onStart { emit(AuthResponseResource.Loading(true)) }
        .onCompletion { emit(AuthResponseResource.Loading(false)) }
        .catch { error ->
            emit(AuthResponseResource.Error(error.message.toString()))
            emit(AuthResponseResource.Loading(false))
        }

    suspend fun requestUserData(token: String): Flow<AuthResponseResource<UserModel>> = flow {
        try {
            val response = AuthService.authService.getUser("Bearer ${token}")
            val body: AuthUserResponse = response.body() ?: AuthUserResponse()
            if (response.code() == 200) {
                emit(AuthResponseResource.Success(body.asModel()))
            } else {
                emit(AuthResponseResource.Error("Token wrong"))
            }
        } catch (e: Exception) {
            emit(AuthResponseResource.Error("Network error: ${e.message.toString()}"))
        }
    }.onStart { emit(AuthResponseResource.Loading(true)) }
        .onCompletion { emit(AuthResponseResource.Loading(false)) }
        .catch { error -> emit(AuthResponseResource.Error(error.message.toString())) }

    suspend fun saveUserData(token: String, user: UserModel): Flow<AuthResponseResource<Boolean>> =
        flow {
            try {
                val response = AuthService.authService.updateUser(
                    token = "Bearer ${token}",
                    name = user.name
                )
                val body: AuthUpdateAnswer = response.body() ?: AuthUpdateAnswer()
                if (response.code() == 200 && body.result == "OK") {
                    emit(AuthResponseResource.Success(true))
                } else {
                    emit(AuthResponseResource.Error(body.result))
                }
            } catch (e: Exception) {
                emit(AuthResponseResource.Error("Network error: ${e.message.toString()}"))
            }
        }.onStart { emit(AuthResponseResource.Loading(true)) }
            .onCompletion { emit(AuthResponseResource.Loading(false)) }
            .catch { error -> emit(AuthResponseResource.Error(error.message.toString())) }
}