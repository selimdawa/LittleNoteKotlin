package com.flatcode.littlenote.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.ActivityAddEditNoteBinding
import com.flatcode.littlenote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private var _binding: ActivityAddEditNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        binding.run {
            toolbar.nameSpace.setText(R.string.add_note)
            toolbar.image.visibility = View.VISIBLE
            toolbar.image.setImageResource(R.drawable.ic_true)
            
            toolbar.image.setOnClickListener {
                val nTitle = noteTitle.text.toString()
                val nContent = noteContent.text.toString()
                if (nTitle.isEmpty() || nContent.isEmpty()) {
                    Toast.makeText(requireContext(), R.string.error_empty, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.addNote(nTitle, nContent)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noteStatus.collect { result ->
                    when (result) {
                        is NoteViewModel.NoteResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is NoteViewModel.NoteResult.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is NoteViewModel.NoteResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}