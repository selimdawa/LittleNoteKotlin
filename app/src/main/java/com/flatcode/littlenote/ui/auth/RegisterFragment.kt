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
import com.flatcode.littlenote.databinding.ActivityRegisterBinding
import com.flatcode.littlenote.utils.DATA
import com.flatcode.littlenote.viewmodel.AuthViewModel
import com.google.firebase.auth.EmailAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: ActivityRegisterBinding? = null
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
        _binding = ActivityRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStatus.collect { result ->
                    when (result) {
                        is AuthViewModel.AuthResult.Loading -> {
                            progressDialog.setMessage("A new account is created...")
                            progressDialog.show()
                        }
                        is AuthViewModel.AuthResult.Success -> {
                            progressDialog.dismiss()
                            showToast(result.message)
                            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
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