package com.flatcode.littlenote.Activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.databinding.ActivityAddEditNoteBinding

class AddNote : AppCompatActivity() {

    private var _binding: ActivityAddEditNoteBinding? = null
    private val binding get() = _binding!!
    private val context: Context = this@AddNote

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                applyTransition()
                finish()
            }
        })

        binding.toolbar.nameSpace.setText(R.string.add_note)
        binding.toolbar.image.visibility = View.VISIBLE
        binding.toolbar.image.setImageResource(R.drawable.ic_true)
        binding.toolbar.image.setOnClickListener {
            val nTitle = binding.noteTitle.text.toString()
            val nContent = binding.noteContent.text.toString()
            if (nTitle.isEmpty() || nContent.isEmpty()) {
                Toast.makeText(context, R.string.error_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE

            val document = DATA.FIREBASE_STORE.collection(DATA.PARENT_PATH)
                .document(DATA.FirebaseUserUid).collection(DATA.CHILD_PATH).document()

            val note = hashMapOf<String, Any>(
                DATA.TITLE to nTitle,
                DATA.CONTENT to nContent
            )

            document.set(note).addOnSuccessListener {
                Toast.makeText(context, R.string.success_note_add, Toast.LENGTH_SHORT).show()
                applyTransition()
                finish()
            }.addOnFailureListener {
                Toast.makeText(context, R.string.error_empty, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
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