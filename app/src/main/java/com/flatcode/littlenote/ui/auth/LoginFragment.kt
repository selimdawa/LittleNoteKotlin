package com.flatcode.littlenote.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.ActivityLoginBinding
import com.flatcode.littlenote.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private val progressDialog: AlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("Please wait...")
            .setView(ProgressBar(requireContext()).apply {
                setPadding(50, 50, 50, 50)
            })
            .setCancelable(false)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.forget.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
        binding.noAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStatus.collect { result ->
                    when (result) {
                        is AuthViewModel.AuthResult.Loading -> {
                            progressDialog.setMessage("Logging In...")
                            progressDialog.show()
                        }
                        is AuthViewModel.AuthResult.Success -> {
                            progressDialog.dismiss()
                            showToast(result.message)
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.alert_delete_title)
            .setMessage(R.string.alert_login_message)
            .setPositiveButton(R.string.alert_login_positive) { _, _ ->
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            .setNegativeButton(R.string.alert_login_negative) { _, _ -> }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}