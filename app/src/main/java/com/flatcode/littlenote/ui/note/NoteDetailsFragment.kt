package com.flatcode.littlenote.ui.note

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.ActivityNoteDetailsBinding
import com.flatcode.littlenote.utils.DATA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {

    private var _binding: ActivityNoteDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        val noteTitle = arguments?.getString(DATA.TITLE)
        val noteContent = arguments?.getString(DATA.CONTENT)
        val colorRes = arguments?.getInt(DATA.COLOR, DATA.DEFAULT_COLOR) ?: DATA.DEFAULT_COLOR
        val docId = arguments?.getString(DATA.ID_PATH)

        binding.toolbar.nameSpace.text = noteTitle
        binding.description.movementMethod = ScrollingMovementMethod()
        binding.description.text = noteContent
        binding.description.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))

        binding.toolbar.image.visibility = View.VISIBLE
        binding.toolbar.image.setImageResource(R.drawable.ic_edit)
        binding.toolbar.image.setOnClickListener {
            val bundle = Bundle().apply {
                putString(DATA.TITLE, noteTitle)
                putString(DATA.CONTENT, noteContent)
                putString(DATA.ID_PATH, docId)
            }
            findNavController().navigate(R.id.action_noteDetailsFragment_to_editNoteFragment, bundle)
        }

        binding.toolbar.add.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}