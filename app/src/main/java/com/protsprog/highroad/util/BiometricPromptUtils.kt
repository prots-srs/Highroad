package com.protsprog.highroad.util

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.protsprog.highroad.R

// Since we are using the same methods in more than one Activity, better give them their own file.
object BiometricPromptUtils {
//    private const val TAG = "TEST_BIOMETRIC
    fun createBiometricPrompt(
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult?) -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errCode, errString)
                processSuccess(null)
//                Log.d(TAG, "errCode is $errCode and errString is: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                processSuccess(null)
//                Log.d(TAG, "Biometric authentication failed for unknown reason.")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
//                Log.d(TAG, "Authentication was successful")
                processSuccess(result)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    fun createPromptInfo(activity: AppCompatActivity): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(activity.getString(R.string.prompt_info_title))
            setSubtitle(activity.getString(R.string.prompt_info_subtitle))
            setDescription(activity.getString(R.string.prompt_info_description))
            setConfirmationRequired(false)
            setNegativeButtonText(activity.getString(R.string.prompt_info_use_app_password))
        }.build()
}