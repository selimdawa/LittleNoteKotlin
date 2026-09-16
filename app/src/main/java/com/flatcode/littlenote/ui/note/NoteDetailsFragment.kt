package com.flatcode.littlenote.ui.note

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.flatcode.littlenote.R
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.databinding.FragmentNoteDetailsBinding
import com.flatcode.littlenote.utils.DATA
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.littlenote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {

    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        val note = arguments?.let {
            BundleCompat.getParcelable(it, DATA.NOTE, Note::class.java)
        }
        var currentNote = note
        val noteId = note?.id ?: -1

        val colorRes = arguments?.getInt(DATA.COLOR, DATA.DEFAULT_COLOR) ?: DATA.DEFAULT_COLOR

        binding.run {
            toolbar.nameSpace.text = currentNote?.title
            description.movementMethod = ScrollingMovementMethod()
            description.text = currentNote?.content
            description.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))

            toolbar.image.visibility = View.VISIBLE
            toolbar.image.setImageResource(R.drawable.ic_edit)
            toolbar.image.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable(DATA.NOTE, currentNote)
                }
                findNavController().navigate(
                    R.id.action_noteDetailsFragment_to_editNoteFragment, bundle
                )
            }

            toolbar.add.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allNotes.collect { notes ->
                    val updatedNote = notes.find { it.id == noteId }
                    if (updatedNote != null) {
                        currentNote = updatedNote
                        binding.toolbar.nameSpace.text = updatedNote.title
                        binding.description.text = updatedNote.content
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