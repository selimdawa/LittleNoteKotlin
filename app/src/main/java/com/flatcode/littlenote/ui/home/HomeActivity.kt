package com.flatcode.littlenote.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flatcode.littlenote.R
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.databinding.ActivityHomeBinding
import com.flatcode.littlenote.ui.adapter.NoteAdapter
import com.flatcode.littlenote.utils.CLASS
import com.flatcode.littlenote.utils.DATA
import com.flatcode.littlenote.utils.VOID
import com.flatcode.littlenote.viewmodel.AuthViewModel
import com.flatcode.littlenote.viewmodel.HomeViewModel
import com.flatcode.littlenote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.MessageFormat

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private var noteAdapter: NoteAdapter? = null
    private val context: Context get() = this

    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                VOID.closeApp(context, this@HomeActivity)
            }
        })

        setupToolbar()
        setupRecyclerView()
        observeViewModels()
    }

    private fun setupToolbar() {
        val currentUser = homeViewModel.currentUser
        if (currentUser != null && currentUser.isAnonymous) {
            binding.toolbar.info.visibility = View.GONE
            binding.toolbar.sync.visibility = View.VISIBLE
        } else {
            binding.toolbar.info.visibility = View.VISIBLE
            binding.toolbar.sync.visibility = View.GONE
        }

        binding.toolbar.sync.setOnClickListener {
            if (currentUser != null && currentUser.isAnonymous) {
                VOID.Intent1(context, CLASS.LOGIN)
                applyTransition()
            } else {
                showToast(getString(R.string.temporary_connect))
            }
        }

        binding.toolbar.add.setOnClickListener {
            VOID.Intent1(context, CLASS.ADD)
            applyTransition()
        }

        binding.toolbar.logout.setOnClickListener { checkUser() }
        binding.toolbar.info.setOnClickListener {
            currentUser?.let {
                VOID.aboutAccount(context, it.displayName, it.email)
            }
        }
    }

    private fun setupRecyclerView() {
        val query = homeViewModel.getNotesQuery() ?: return

        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()

        noteAdapter = NoteAdapter(context, options, { count ->
            binding.toolbar.number.text = MessageFormat.format(" ({0})", count)
        }, { docId ->
            noteViewModel.deleteNote(docId)
        })

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = noteAdapter
    }

    private fun observeViewModels() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteViewModel.noteStatus.collect { result ->
                    if (result is NoteViewModel.NoteResult.Success) {
                        showToast(result.message)
                    } else if (result is NoteViewModel.NoteResult.Error) {
                        showToast(result.message)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authStatus.collect { result ->
                    if (result is AuthViewModel.AuthResult.Success && result.message == "User deleted") {
                        VOID.Intent1(context, CLASS.SPLASH)
                        applyTransition()
                        finish()
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
            VOID.Intent1(context, CLASS.SPLASH)
            applyTransition()
            finish()
        }
    }

    private fun displayAlert() {
        AlertDialog.Builder(context)
            .setTitle(R.string.alert_delete_title)
            .setMessage(R.string.alert_delete_message)
            .setPositiveButton(R.string.alert_delete_positive) { _, _ ->
                VOID.Intent1(context, CLASS.REGISTER)
                finish()
            }
            .setNegativeButton(R.string.alert_delete_negative) { _, _ ->
                authViewModel.deleteAnonymousUser(homeViewModel.currentUser?.uid ?: "")
            }.show()
    }

    override fun onStart() {
        super.onStart()
        noteAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter?.stopListening()
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

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}