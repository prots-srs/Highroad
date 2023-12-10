package com.protsprog.highroad

import android.security.keystore.KeyProperties
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.protsprog.highroad.authentication.domen.AuthAppLogin
import com.protsprog.highroad.util.CryptographyManager
import com.protsprog.highroad.authentication.LoginState
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

@RunWith(AndroidJUnit4::class)
class CipherTest {
    private val KEY_SIZE = 256
    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

    private val cryptographyManager = CryptographyManager()

    @Before
    fun cipher_setting() {
        AuthAppLogin.apply {
            token = "56|5496598i650djfirmgreoebrjnr"
            email = "test@test.ua"
            password = "okko"
        }
    }

    @Test
    fun simple_sipher_test() {
        val login = LoginState(
            email = AuthAppLogin.email ?: "", password = AuthAppLogin.password ?: ""
        )
        Log.d("TEST_CIPHER", "login: ${login}")

        val json = Json.encodeToString(login)
//        Log.d("TEST_CIPHER", "json: ${json}")

        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(KEY_SIZE)
        val key: SecretKey = keygen.generateKey()
        val cipherEncrypt = getCipher()
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, key)

        val cipherWrapper = cryptographyManager.encryptData(json, cipherEncrypt)
//        Log.d("TEST_CIPHER", "cipherWrapper: ${cipherWrapper}")

        val cipherDecrypt = getCipher()
        cipherDecrypt.init(
            Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, cipherWrapper.initializationVector)
        )

        val ciphertextDecrypt =
            cryptographyManager.decryptData(cipherWrapper.ciphertext, cipherDecrypt)
        val decryptLogin = Json.decodeFromString<LoginState>(ciphertextDecrypt)
        Log.d(
            "TEST_CIPHER", "ciphertextDecrypt string: ${ciphertextDecrypt}"
        )
        Log.d(
            "TEST_CIPHER", "decryptLogin: ${decryptLogin}"
        )
    }

    private fun getCipher(): Cipher {
        //"AES/CBC/PKCS5PADDING"
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

}