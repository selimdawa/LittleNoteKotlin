package com.flatcode.littlenote.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.FragmentSplashBinding
import com.flatcode.littlenote.utils.DATA
import com.flatcode.littlenote.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthStatus()
        viewModel.checkUserAndRedirect(DATA.DELAY_LOG.toLong())
    }

    private fun observeAuthStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStatus.collect { result ->
                    when (result) {
                        is AuthViewModel.AuthResult.Authenticated -> {
                            goToHome()
                        }
                        is AuthViewModel.AuthResult.Success -> {
                            if (result.message == "Anonymous Login Successful") {
                                context?.let {
                                    Toast.makeText(it, R.string.temporary_log, Toast.LENGTH_LONG).show()
                                }
                                goToHome()
                            }
                        }
                        is AuthViewModel.AuthResult.Error -> {
                            context?.let {
                                Toast.makeText(it, "${getString(R.string.error_log)}${result.message}", Toast.LENGTH_SHORT).show()
                            }
                            activity?.finish()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun goToHome() = findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}