package com.flatcode.littlenote.Auth

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityRegisterBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class Register : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@Register
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
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener { VOID.Intent1(context, CLASS.LOGIN) }

        binding.go.setOnClickListener {
            progressDialog.setMessage("A new account is created...")

            val username = binding.nameEt.text.toString()
            val userEmail = binding.emailEt.text.toString()
            val userPass = binding.cPasswordEt.text.toString()
            val confirmPass = binding.passwordEt.text.toString()

            if (userEmail.isEmpty() || username.isEmpty() || userPass.isEmpty() || confirmPass.isEmpty()) {
                showToast(getString(R.string.empty_required_all))
                return@setOnClickListener
            } else if (userPass != confirmPass) {
                binding.passwordEt.error = DATA.ERR_PASS
            } else {
                progressDialog.show()
                val credential = EmailAuthProvider.getCredential(userEmail, userPass)

                auth.currentUser?.linkWithCredential(credential)
                    ?.addOnSuccessListener {
                        showToast(getString(R.string.notes_synced))
                        val usr = auth.currentUser
                        val request = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                        usr?.updateProfile(request)

                        VOID.Intent1(context, CLASS.HOME)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            overrideActivityTransition(
                                OVERRIDE_TRANSITION_OPEN,
                                R.anim.slide_up,
                                R.anim.slide_down
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
                        }

                        finish()
                    }
                    ?.addOnFailureListener {
                        showToast(getString(R.string.failed_connect))
                        progressDialog.dismiss()
                    }
            }
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