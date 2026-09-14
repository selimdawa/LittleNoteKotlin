package com.flatcode.littlenote.ui.splash

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.ActivitySplashBinding
import com.flatcode.littlenote.utils.CLASS
import com.flatcode.littlenote.utils.DATA
import com.flatcode.littlenote.utils.VOID
import com.flatcode.littlenote.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private val context: Context = this@SplashActivity
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeAuthStatus()

        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.currentUser != null) {
                goToHome()
            } else {
                viewModel.signInAnonymously()
            }
        }, DATA.DELAY_LOG.toLong())
    }

    private fun observeAuthStatus() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStatus.collect { result ->
                    when (result) {
                        is AuthViewModel.AuthResult.Success -> {
                            if (result.message == "Anonymous Login Successful") {
                                Toast.makeText(context, R.string.temporary_log, Toast.LENGTH_LONG).show()
                                goToHome()
                            }
                        }
                        is AuthViewModel.AuthResult.Error -> {
                            Toast.makeText(context, "${getString(R.string.error_log)}${result.message}", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun goToHome() {
        VOID.Intent1(context, CLASS.HOME)
        applyTransition()
        finish()
    }

    private fun applyTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_up,
                R.anim.slide_down
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}