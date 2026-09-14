package com.flatcode.littlenote.ui.auth

import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.ActivityLoginBinding
import com.flatcode.littlenote.utils.CLASS
import com.flatcode.littlenote.utils.VOID
import com.flatcode.littlenote.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@LoginActivity
    private val viewModel: AuthViewModel by viewModels()

    private val progressDialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setTitle("Please wait...")
            .setView(ProgressBar(context).apply {
                setPadding(50, 50, 50, 50)
            })
            .setCancelable(false)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showWarning()

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast(getString(R.string.empty_required))
                return@setOnClickListener
            }

            viewModel.signIn(email, password)
        }

        binding.forget.setOnClickListener { VOID.Intent1(context, CLASS.FORGET_PASSWORD) }
        binding.noAccount.setOnClickListener { VOID.Intent1(context, CLASS.REGISTER) }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStatus.collect { result ->
                    when (result) {
                        is AuthViewModel.AuthResult.Loading -> {
                            progressDialog.setMessage("Logging In...")
                            progressDialog.show()
                        }
                        is AuthViewModel.AuthResult.Success -> {
                            progressDialog.dismiss()
                            showToast(result.message)
                            VOID.IntentClear(context, CLASS.HOME)
                            finish()
                        }
                        is AuthViewModel.AuthResult.Error -> {
                            progressDialog.dismiss()
                            showToast(result.message)
                        }
                        else -> {}
                    }
                }
            }
        }
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