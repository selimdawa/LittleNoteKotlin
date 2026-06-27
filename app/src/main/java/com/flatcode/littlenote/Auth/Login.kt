package com.flatcode.littlenote.Auth

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@Login
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val store: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

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
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showWarning()

        binding.loginBtn.setOnClickListener {
            progressDialog.setMessage("Logging In...")
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast(getString(R.string.empty_required))
                return@setOnClickListener
            }

            progressDialog.show()
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                showToast(getString(R.string.login_success))

                val currentUser = auth.currentUser
                if (currentUser != null && currentUser.isAnonymous) {
                    store.collection(DATA.PARENT_PATH).document(currentUser.uid).delete()
                        .addOnSuccessListener {
                            showToast(getString(R.string.delete_message_notes))
                        }

                    currentUser.delete().addOnSuccessListener {
                        showToast(getString(R.string.delete_message_user))
                    }
                }
                VOID.IntentClear(context, CLASS.HOME)
                finish()
            }.addOnFailureListener { e: Exception ->
                showToast("${getString(R.string.login_failure)}${e.message}")
                progressDialog.dismiss()
            }
        }

        binding.forget.setOnClickListener { VOID.Intent1(context, CLASS.FORGET_PASSWORD) }
        binding.noAccount.setOnClickListener { VOID.Intent1(context, CLASS.REGISTER) }
    }

    private fun showWarning() {
        AlertDialog.Builder(context)
            .setTitle(R.string.alert_delete_title)
            .setMessage(R.string.alert_login_message)
            .setPositiveButton(R.string.alert_login_positive) { _, _ ->
                VOID.Intent1(context, CLASS.REGISTER)
                finish()
            }
            .setNegativeButton(R.string.alert_login_negative) { _, _ -> }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}