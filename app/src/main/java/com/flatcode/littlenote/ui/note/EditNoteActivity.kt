package com.flatcode.littlenote.ui.note

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.ActivityAddEditNoteBinding
import com.flatcode.littlenote.utils.DATA
import com.flatcode.littlenote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {

    private var _binding: ActivityAddEditNoteBinding? = null
    private val binding get() = _binding!!
    private val context: Context = this@EditNoteActivity
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                applyTransition()
                finish()
            }
        })

        val noteTitle = intent.getStringExtra(DATA.TITLE)
        val noteContent = intent.getStringExtra(DATA.CONTENT)
        val docId = intent.getStringExtra(DATA.ID_PATH) ?: ""

        binding.toolbar.nameSpace.setText(R.string.edit_note)
        binding.toolbar.image.visibility = View.VISIBLE
        binding.toolbar.image.setImageResource(R.drawable.ic_true)
        binding.noteTitle.setText(noteTitle)
        binding.noteContent.setText(noteContent)

        binding.toolbar.image.setOnClickListener {
            val nTitle = binding.noteTitle.text.toString()
            val nContent = binding.noteContent.text.toString()
            if (nTitle.isEmpty() || nContent.isEmpty()) {
                Toast.makeText(context, R.string.error_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.editNote(docId, nTitle, nContent)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noteStatus.collect { result ->
                    when (result) {
                        is NoteViewModel.NoteResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is NoteViewModel.NoteResult.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            applyTransition()
                            finish()
                        }
                        is NoteViewModel.NoteResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }
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