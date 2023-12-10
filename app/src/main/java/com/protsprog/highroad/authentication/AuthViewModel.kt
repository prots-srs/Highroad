package com.protsprog.highroad.authentication

/*
TO READ
https://developer.android.com/codelabs/android-baseline-profiles-improve?hl=en#0

https://developer.android.com/design/ui/mobile/guides/patterns/passkeys

https://laravel.com/docs/10.x/authorization
https://laravel.com/docs/10.x/sanctum

 */
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protsprog.highroad.authentication.domen.AuthAppLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

const val demoUser1Email = "testlogin@protsprog.com"
const val demoUser1Password = "test0token"

const val demoUser2Email = "prots.srs@gmail.com"
const val demoUser2Password = "mars13uran"

data class UserState(
    val name: String = "", val email: String = ""
)

@Serializable
data class LoginState(
    val email: String = "", val password: String = ""
)

data class AuthUIState(
    val hasAuth: Boolean = false, val sendRequest: Boolean = false, val errorLogin: String = "",
    val showBiometricButton: Boolean = false
)

interface AuthServices {
    val onClickLogin: () -> Unit
    val onClickProfile: () -> Unit
    val onClickLogout: () -> Unit
    val name: String
    val email: String
    val hasAuthorization: Boolean
}

class StateActionsAuthTopBar(
    override val onClickLogin: () -> Unit,
    override val onClickProfile: () -> Unit,
    override val onClickLogout: () -> Unit,
    override val name: String,
    override val email: String,
    override val hasAuthorization: Boolean
) : AuthServices

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val authRepo: AuthRepository
) : ViewModel() {

    var authUIState by mutableStateOf(AuthUIState())
    var loginUIState by mutableStateOf(LoginState())
    var userUIState by mutableStateOf(UserState())

    fun onChangeEmail(input: String = "") {
        if (input.length < 30) {
            loginUIState = loginUIState.copy(
                email = input
            )
        }
    }

    fun setStateBiometricButton(show: Boolean) {
        authUIState = authUIState.copy(
            showBiometricButton = show
        )
    }

    fun onChangePassword(input: String = "") {
        if (input.length < 15) {
            loginUIState = loginUIState.copy(
                password = input
            )
        }
    }

    fun onClickLogout() {
        AuthAppLogin.apply {
            token = null
            email = null
            password = null
        }

        clearLoginForm()
    }

    fun onSubmitLogin(biometricPrompt: () -> Unit = {}) {
        if (!validateLoginData(loginUIState.email, loginUIState.password)) {
            return
        }
        serverLogin(loginUIState.email, loginUIState.password, biometricPrompt)
    }


    private fun validateLoginData(email: String, password: String): Boolean {
        if (email.length == 0 || password.length == 0) {
            authUIState = authUIState.copy(
                errorLogin = "Some fields is empty"
            )
            return false
        } else {
            authUIState = authUIState.copy(
                errorLogin = ""
            )
            return true
        }
    }

    private fun serverLogin(email: String, password: String, biometricPrompt: () -> Unit = {}) {/*val loginResponse = authRepo.requestLogin(authUIStates.user.email, authUIStates.password)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AuthResponseResource.Loading(true)
            )*/

        authUIState = authUIState.copy(
            errorLogin = ""
        )
        AuthAppLogin.token = null
        viewModelScope.launch {
            authRepo.requestLogin(email, password).collect { responceToken ->
                when (responceToken) {
                    is AuthResponseResource.Loading -> {
                        authUIState = authUIState.copy(
                            sendRequest = responceToken.status
                        )
                    }

                    is AuthResponseResource.Error -> {
                        authUIState = authUIState.copy(
                            errorLogin = responceToken.error
                        )
                    }

                    is AuthResponseResource.Success -> {
                        AuthAppLogin.apply {
                            this.token = responceToken.data
                            this.email = email
                            this.password = password
                        }

                        authUIState = authUIState.copy(
                            hasAuth = true,
                        )

                        getUserData()

                        biometricPrompt()
                    }
                }
            }
        }
    }

    private suspend fun getUserData() {
        AuthAppLogin.token?.let { token ->
            authRepo.requestUserData(token).collect { responceUser ->
                when (responceUser) {
                    is AuthResponseResource.Loading -> {
                        authUIState = authUIState.copy(
                            sendRequest = responceUser.status
                        )
                    }

                    is AuthResponseResource.Error -> {
                        userUIState = userUIState.copy(
                            name = responceUser.error,
                            email = "-",
                        )
                    }

                    is AuthResponseResource.Success -> {
                        userUIState = userUIState.copy(
                            name = responceUser.data.name,
                            email = responceUser.data.email,
                        )
                    }
                }
            }
        }
    }

    fun clearLoginForm() {
        authUIState = authUIState.copy(
            sendRequest = false,
            errorLogin = "",
            hasAuth = false,
        )
//        loginUIState = loginUIState.copy(
//            email = "",
//            password = ""
//        )
        userUIState = userUIState.copy(
            name = "",
            email = "",
        )
    }

    fun updateUserData() {
        viewModelScope.launch {
            getUserData()
        }
    }

    fun saveUserData(name: String) {
        if (name.isEmpty()) {
            authUIState = authUIState.copy(
                errorLogin = "Name field is empty"
            )
            return
        }

        authUIState = authUIState.copy(
            errorLogin = ""
        )
        viewModelScope.launch {
            AuthAppLogin.token?.let { token ->
                authRepo.saveUserData(token, UserModel(name = name, email = ""))
                    .collect { response ->
                        when (response) {
                            is AuthResponseResource.Loading -> {
                                authUIState = authUIState.copy(
                                    sendRequest = response.status
                                )
                            }

                            is AuthResponseResource.Error -> {
                                authUIState = authUIState.copy(
                                    errorLogin = response.error
                                )
                            }

                            is AuthResponseResource.Success -> {
                                getUserData()
                            }
                        }
                    }
            }
        }
    }

    fun biometricLogin(callback: () -> Unit = {}) {

        val email = AuthAppLogin.email ?: ""
        val password = AuthAppLogin.password ?: ""

        if (!validateLoginData(email, password)) {
            return
        }

        serverLogin(email, password, callback)
    }

    fun moveTestAuth() {
        serverLogin(demoUser2Email, demoUser2Password)
    }
}