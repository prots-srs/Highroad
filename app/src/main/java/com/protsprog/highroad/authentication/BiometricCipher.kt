package com.protsprog.highroad.authentication

/*
TO READ

https://developer.android.com/training/sign-in/biometric-auth
https://developer.android.com/training/articles/keystore

https://developer.android.com/reference/kotlin/javax/crypto/Cipher#dofinal_3
https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.AuthenticationResult
 */

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.protsprog.highroad.R
import com.protsprog.highroad.authentication.domen.AuthAppLogin
import com.protsprog.highroad.authentication.ui.LoginState
import com.protsprog.highroad.util.BiometricPromptUtils
import com.protsprog.highroad.util.CIPHERTEXT_WRAPPER
import com.protsprog.highroad.util.CryptographyManager
import com.protsprog.highroad.util.SHARED_PREFS_FILENAME
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface BiometricCryption {

    val canUseBiometric: Boolean
    fun showBiometricPromptForEncryption(viewCallback: () -> Unit)
    fun showBiometricPromptForDecryption(viewCallback: () -> Unit)

    fun inNeedShowBiometricButton(): Boolean
    fun getBiometricStrings(): BiometricManager.Strings?
}

class BiometricCipher(
    private val appContext: Context,
    private val curActivity: AppCompatActivity
) : BiometricCryption {

    val authenticatorType = BiometricManager.Authenticators.BIOMETRIC_STRONG// or DEVICE_CREDENTIAL

    private val biometricManager: BiometricManager = BiometricManager.from(appContext)
    private val promptInfo: BiometricPrompt.PromptInfo =
        BiometricPromptUtils.createPromptInfo(curActivity)

    private lateinit var biometricPrompt: BiometricPrompt

    override val canUseBiometric: Boolean
        get() = biometricManager.canAuthenticate(authenticatorType) == BiometricManager.BIOMETRIC_SUCCESS

    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            appContext, SHARED_PREFS_FILENAME, Context.MODE_PRIVATE, CIPHERTEXT_WRAPPER
        )

    private var viewModelCallback: () -> Unit = {}

    override fun showBiometricPromptForEncryption(viewCallback: () -> Unit) {
        if (!canUseBiometric) {
            return
        }

        viewModelCallback = viewCallback

        val cipher = cryptographyManager.getInitializedCipherForEncryption(curActivity.getString(R.string.secret_key_name))
        biometricPrompt = BiometricPromptUtils.createBiometricPrompt(
            curActivity, ::encryptAndStoreServerLogin
        )
        //show window
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    private fun encryptAndStoreServerLogin(authResult: BiometricPrompt.AuthenticationResult?) {
        if (authResult == null) {
            viewModelCallback()
        } else {
            authResult.cryptoObject?.cipher?.apply {

                AuthAppLogin.token.let { token ->
                    try {
                        val jsonLogin = Json.encodeToString(
                            LoginState(
                                email = AuthAppLogin.email ?: "",
                                password = AuthAppLogin.password ?: ""
                            )
                        )

                        val encryptedServerLoginWrapper =
                            cryptographyManager.encryptData(jsonLogin, this)

                        cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                            encryptedServerLoginWrapper,
                            appContext,
                            SHARED_PREFS_FILENAME,
                            Context.MODE_PRIVATE,
                            CIPHERTEXT_WRAPPER
                        )

                        viewModelCallback()
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    override fun showBiometricPromptForDecryption(viewCallback: () -> Unit) {
        if (!canUseBiometric) {
            return
        }

        viewModelCallback = viewCallback

        ciphertextWrapper?.let { textWrapper ->
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                curActivity.getString(R.string.secret_key_name), textWrapper.initializationVector
            )
            biometricPrompt = BiometricPromptUtils.createBiometricPrompt(
                curActivity, processSuccess = ::decryptServerLoginFromStorage
            )
            //show window
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }

    }


    private fun decryptServerLoginFromStorage(authResult: BiometricPrompt.AuthenticationResult?) {
        if(authResult == null) {
//            viewModelCallback()
        } else {
            ciphertextWrapper?.let { textWrapper ->
                authResult.cryptoObject?.cipher?.let {
                    try {
                        val ciphertextDecrypt =
                            cryptographyManager.decryptData(textWrapper.ciphertext, it)
                        val decryptLogin = Json.decodeFromString<LoginState>(ciphertextDecrypt)

//                    Log.d("TEST_BIOMETRIC", "login: ${decryptLogin}")

                        AuthAppLogin.apply {
                            email = decryptLogin.email
                            password = decryptLogin.password
                        }

                        Log.d("TEST_BIOMETRIC", "login de: ${decryptLogin}")

                        viewModelCallback()
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    override fun inNeedShowBiometricButton(): Boolean = canUseBiometric && ciphertextWrapper != null
    override fun getBiometricStrings(): BiometricManager.Strings? =
        biometricManager.getStrings(authenticatorType)
}