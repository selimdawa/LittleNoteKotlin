package com.flatcode.littlenote.ui.auth

import android.os.Bundle
import android.util.Patterns
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
import com.flatcode.littlenote.databinding.ActivityForgetPasswordBinding
import com.flatcode.littlenote.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    private var _binding: ActivityForgetPasswordBinding? = null
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
        _binding = ActivityForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noAccount.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPasswordFragment_to_registerFragment)
        }

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
        }

        binding.go.setOnClickListener { validateData() }

        observeViewModel()
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
                viewModel.resetPassword(email)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStatus.collect { result ->
                    when (result) {
                        is AuthViewModel.AuthResult.Loading -> {
                            progressDialog.setMessage("Sending recovery email...")
                            progressDialog.show()
                        }
                        is AuthViewModel.AuthResult.Success -> {
                            progressDialog.dismiss()
                            showToast(result.message)
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}