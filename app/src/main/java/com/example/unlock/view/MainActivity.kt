package com.example.unlock.view

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.unlock.Hello
import com.example.unlock.R
import com.example.unlock.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executor


open class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 1010

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        fingerScan()
        binding.btnBack.setOnClickListener {
            display

        }
        binding.btnParten.setOnClickListener {
            startActivity(Intent(this@MainActivity, Password::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fingerScan() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                binding.errorText.text = ""
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                binding.errorText.run {
                    text = "No biometric features available on this device."
                    binding.errorText.setTextColor(
                        resources.getColor(
                            R.color.fingerprint_fail,
                            null
                        )
                    )
                }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                binding.errorText.run {
                    text = "Biometric features are currently unavailable."
                    binding.errorText.setTextColor(
                        resources.getColor(
                            R.color.fingerprint_fail,
                            null
                        )
                    )
                }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
           {
               binding.errorText.run {
                   text = "Error msg biometric not setup"
               }
                val  enrolllntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                startActivityForResult(enrolllntent,REQUEST_CODE)
                }

        }

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS) {
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(
                            applicationContext,
                            "$errString", Toast.LENGTH_SHORT
                        )
                            .show()

                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        binding.icon.setImageResource(R.drawable.ic_group_check)
                        binding.errorText.run {
                            text = "Unlock Successfully"
                            binding.errorText.setTextColor(
                                binding.errorText.resources.getColor(
                                    R.color.green_00B,
                                    null
                                )
                            )
                            binding.view1.setBackgroundResource(R.drawable.gradientthree)
                            postDelayed(null, 1000)
                        }
                        startActivity(Intent(this@MainActivity, Hello::class.java))
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        showError()

                    }


                })
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build()
            biometricPrompt.authenticate(promptInfo)
            binding.icon.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showError() {
        binding.icon.setImageResource(R.drawable.ic_group_374)
        binding.errorText.run {
            binding.view1.setBackgroundResource(R.drawable.gradienttwo)
            text = "Fingerprints unlock failed!"
            binding.errorText.setTextColor(
                binding.errorText.resources.getColor(
                    R.color.fingerprint_fail,
                    null
                )
            )
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                binding.icon.setImageResource(R.drawable.ic_group_373)
                binding.view1.setBackgroundResource(R.drawable.gradientone)
                binding.errorText.run {
                    setTextColor(binding.errorText.resources.getColor(R.color.fingerprint, null))
                    text = ""
                }
            }
        }
    }
}






