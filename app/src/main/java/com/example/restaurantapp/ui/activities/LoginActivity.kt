package com.example.restaurantapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        binding.btnLoginEnter.setOnClickListener {
            logInRegisteredUser()
        }
    }

    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {

            showProgressDialog()

            val androidNumber = binding.etLoginEmail.text.toString().toInt()
            var android = ""

            android = if (androidNumber <= 9) {
                "00$androidNumber@gmail.com"
            } else {
                "0$androidNumber@gmail.com"
            }

            val email = android
            val password = binding.etLoginPassword.text.toString().trim() { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userLoggedInSuccess()
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    private fun userLoggedInSuccess() {
        hideProgressDialog()
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etLoginEmail.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_android_number), true)
                false
            }

            TextUtils.isEmpty(binding.etLoginPassword.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_android_password),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }
}