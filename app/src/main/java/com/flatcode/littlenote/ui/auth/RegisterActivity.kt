package com.flatcode.littlenote.ui.auth

import android.content.Context
import android.os.Build
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
import com.flatcode.littlenote.databinding.ActivityRegisterBinding
import com.flatcode.littlenote.utils.CLASS
import com.flatcode.littlenote.utils.DATA
import com.flatcode.littlenote.utils.VOID
import com.flatcode.littlenote.viewmodel.AuthViewModel
import com.google.firebase.auth.EmailAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@RegisterActivity
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
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener { VOID.Intent1(context, CLASS.LOGIN) }

        binding.go.setOnClickListener {
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
                val credential = EmailAuthProvider.getCredential(userEmail, userPass)
                viewModel.linkCredential(credential, username)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStatus.collect { result ->
                    when (result) {
                        is AuthViewModel.AuthResult.Loading -> {
                            progressDialog.setMessage("A new account is created...")
                            progressDialog.show()
                        }
                        is AuthViewModel.AuthResult.Success -> {
                            progressDialog.dismiss()
                            showToast(result.message)
                            VOID.Intent1(context, CLASS.HOME)
                            applyTransition()
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

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun applyTransition() {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}