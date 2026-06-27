package com.flatcode.littlenote.Auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {

    private var _binding: ActivityForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@ForgetPassword
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val progressDialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setTitle("Please wait...")
            .setView(android.widget.ProgressBar(context).apply {
                setPadding(50, 50, 50, 50)
            })
            .setCancelable(false)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)

        _binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noAccount.setOnClickListener {
            VOID.Intent1(context, CLASS.REGISTER)
            finish()
        }

        binding.login.setOnClickListener {
            VOID.Intent1(context, CLASS.LOGIN)
            finish()
        }

        binding.go.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.emailEt.text.toString().trim()

        when {
            email.isEmpty() -> {
                showToast("Enter email...!")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid email format...!")
            }
            else -> {
                recoverPassword(email)
            }
        }
    }

    private fun recoverPassword(email: String) {
        progressDialog.setMessage("Sending password recovery instructions to $email")
        progressDialog.show()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    showToast("Instructions to reset password sent to $email")
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                showToast("Failed to send due to ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}