package com.flatcode.littlenote.ui.home

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flatcode.littlenote.R
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.databinding.ActivityHomeBinding
import com.flatcode.littlenote.databinding.DialogAboutAccountBinding
import com.flatcode.littlenote.databinding.DialogCloseAppBinding
import com.flatcode.littlenote.ui.adapter.NoteAdapter
import com.flatcode.littlenote.utils.DATA
import com.flatcode.littlenote.viewmodel.AuthViewModel
import com.flatcode.littlenote.viewmodel.HomeViewModel
import com.flatcode.littlenote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.MessageFormat

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private var noteAdapter: NoteAdapter? = null

    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showCloseAppDialog()
            }
        })

        setupToolbar()
        setupRecyclerView()
        observeViewModels()
    }

    private fun setupToolbar() {
        val currentUser = homeViewModel.currentUser
        binding.toolbar.run {
            if (currentUser != null && currentUser.isAnonymous) {
                info.visibility = View.GONE
                sync.visibility = View.VISIBLE
            } else {
                info.visibility = View.VISIBLE
                sync.visibility = View.GONE
            }

            sync.setOnClickListener {
                if (currentUser != null && currentUser.isAnonymous) {
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                } else {
                    showToast(getString(R.string.temporary_connect))
                }
            }

            add.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
            }

            logout.setOnClickListener { checkUser() }
            info.setOnClickListener {
                currentUser?.let {
                    showAboutAccountDialog(it.displayName, it.email)
                }
            }
        }
    }

    private fun showCloseAppDialog() {
        val dialogBinding = DialogCloseAppBinding.inflate(layoutInflater)
        Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialogBinding.yes.setOnClickListener { requireActivity().finish() }
            dialogBinding.no.setOnClickListener { dismiss() }
            show()
        }
    }

    private fun showAboutAccountDialog(username: String?, email: String?) {
        val dialogBinding = DialogAboutAccountBinding.inflate(layoutInflater)
        Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialogBinding.username.text = username
            dialogBinding.email.text = email
            show()
        }
    }

    private fun setupRecyclerView() {
        val query = homeViewModel.getNotesQuery() ?: return

        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()

        noteAdapter = NoteAdapter(requireContext(), options, { count ->
            binding.toolbar.number.text = MessageFormat.format(" ({0})", count)
        }, { docId ->
            noteViewModel.deleteNote(docId)
        }, { note, docId, color ->
            val bundle = Bundle().apply {
                putString(DATA.TITLE, note.title)
                putString(DATA.CONTENT, note.content)
                putString(DATA.ID_PATH, docId)
                putInt(DATA.COLOR, color)
            }
            findNavController().navigate(R.id.action_homeFragment_to_noteDetailsFragment, bundle)
        }, { note, docId ->
            val bundle = Bundle().apply {
                putString(DATA.TITLE, note.title)
                putString(DATA.CONTENT, note.content)
                putString(DATA.ID_PATH, docId)
            }
            findNavController().navigate(R.id.action_homeFragment_to_editNoteFragment, bundle)
        })

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = noteAdapter
        noteAdapter?.startListening()
    }

    private fun observeViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteViewModel.noteStatus.collect { result ->
                    if (result is NoteViewModel.NoteResult.Success) {
                        showToast(result.message)
                    } else if (result is NoteViewModel.NoteResult.Error) {
                        showToast(result.message)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authStatus.collect { result ->
                    if (result is AuthViewModel.AuthResult.Success && result.message == "User deleted") {
                        findNavController().navigate(R.id.action_homeFragment_to_splashFragment)
                    }
                }
            }
        }
    }

    private fun checkUser() {
        val currentUser = homeViewModel.currentUser
        if (currentUser != null && currentUser.isAnonymous) {
            displayAlert()
        } else {
            homeViewModel.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_splashFragment)
        }
    }

    private fun displayAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.alert_delete_title)
            .setMessage(R.string.alert_delete_message)
            .setPositiveButton(R.string.alert_delete_positive) { _, _ ->
                findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
            }
            .setNegativeButton(R.string.alert_delete_negative) { _, _ ->
                authViewModel.deleteAnonymousUser(homeViewModel.currentUser?.uid ?: "")
            }.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        noteAdapter?.stopListening()
        _binding = null
    }
}