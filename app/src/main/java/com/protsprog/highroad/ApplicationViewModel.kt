package com.protsprog.highroad

/*
TO READ
https://developer.android.com/codelabs/android-baseline-profiles-improve?hl=en#0

https://developer.android.com/design/ui/mobile/guides/patterns/passkeys

https://laravel.com/docs/10.x/authorization
https://laravel.com/docs/10.x/sanctum

https://betterprogramming.pub/android-access-token-logic-with-retrofit-okhttp-interceptors-740ea48547a0

https://square.github.io/okhttp/
 */
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protsprog.highroad.authentication.data.AuthRepository
import com.protsprog.highroad.authentication.data.AuthResponseResource
import com.protsprog.highroad.authentication.data.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserUIState(
    val name: String = "",
    val email: String = "testlogin@protsprog.com",
)

data class AuthUIState(
    val hasAuth: Boolean = false,
    val sendRequest: Boolean = false,
    val password: String = "123",//"",
    val user: UserUIState = UserUIState(),
    val errorLogin: String = ""
)

data class AuthOptionsMenuModel(
    val title: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit = {}
)

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authRepo: AuthRepository
) : ViewModel() {
    var authUIStates by mutableStateOf(AuthUIState())

    var authToken: String = ""

    fun onChangeEmail(input: String = "") {
        if (input.length < 30) {
            authUIStates = authUIStates.copy(
                user = authUIStates.user.copy(
                    email = input
                )
            )
        }
    }

    fun onChangePassword(input: String = "") {
        if (input.length < 15) {
            authUIStates = authUIStates.copy(
                password = input
            )
        }
    }

    fun onSubmitLogin() {
        validateLoginData()
    }

    private fun validateLoginData() {

        if (authUIStates.user.email.length == 0 || authUIStates.password.length == 0) {
            authUIStates = authUIStates.copy(
                errorLogin = "Some fields is empty"
            )
            return
        }

        /*val loginResponse = authRepo.requestLogin(authUIStates.user.email, authUIStates.password)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AuthResponseResource.Loading(true)
            )*/

        authUIStates = authUIStates.copy(
            errorLogin = ""
        )
        viewModelScope.launch {
            authRepo.requestLogin(authUIStates.user.email, authUIStates.password)
                .collect { responceToken ->
                    when (responceToken) {
                        is AuthResponseResource.Loading -> {
                            authUIStates = authUIStates.copy(
                                sendRequest = responceToken.status
                            )
                        }

                        is AuthResponseResource.Error -> {
                            authUIStates = authUIStates.copy(
                                errorLogin = responceToken.error
                            )
                        }

                        is AuthResponseResource.Success -> {
                            authToken = responceToken.data
                            authUIStates = authUIStates.copy(
                                hasAuth = true
                            )
                            getUserData()
                        }
                    }
                }
        }
    }

    suspend fun getUserData() {
        authRepo.requestUserData(authToken).collect { responceUser ->
            when (responceUser) {
                is AuthResponseResource.Loading -> {
                    authUIStates = authUIStates.copy(
                        sendRequest = responceUser.status
                    )
                }

                is AuthResponseResource.Error -> {}
                is AuthResponseResource.Success -> {
                    authUIStates = authUIStates.copy(
                        user = authUIStates.user.copy(
                            name = responceUser.data.name,
                            email = responceUser.data.email,
                        )
                    )
                }
            }
        }
    }

    fun clearLoginForm() {
        authUIStates = authUIStates.copy(
            sendRequest = false,
            errorLogin = "",
            password = "",
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateUserData() {
        viewModelScope.launch {
            getUserData()
        }
    }

    fun saveUserData(name: String): Boolean {
        if (name.isEmpty()) return false

        authUIStates = authUIStates.copy(
            user = authUIStates.user.copy(
                name = name,
            )
        )

        viewModelScope.launch {
            authRepo.saveUserData(
                UserModel(
                    email = "",
                    name = name
                )
            )
//            getUserData()
        }

        return true
    }
}