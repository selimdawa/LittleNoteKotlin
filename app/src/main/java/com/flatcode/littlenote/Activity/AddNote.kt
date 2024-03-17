package com.flatcode.littlenote.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityAddEditNoteBinding

class AddNote : AppCompatActivity() {

    private var binding: ActivityAddEditNoteBinding? = null
    private val context: Context = this@AddNote

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.add_note)
        binding!!.toolbar.image.visibility = View.VISIBLE
        binding!!.toolbar.image.setImageResource(R.drawable.ic_true)
        binding!!.toolbar.image.setOnClickListener {
            val nTitle = binding!!.noteTitle.text.toString()
            val nContent = binding!!.noteContent.text.toString()
            if (nTitle.isEmpty() || nContent.isEmpty()) {
                Toast.makeText(context, R.string.error_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding!!.progressBar.visibility = View.VISIBLE

            // save note
            val document =
                DATA.FIREBASE_STORE.collection(DATA.PARENT_PATH).document(DATA.FirebaseUserUid)
                    .collection(DATA.CHILD_PATH).document()
            val note: MutableMap<String?, Any> = HashMap()
            note[DATA.TITLE] = nTitle
            note[DATA.CONTENT] = nContent
            document.set(note).addOnSuccessListener {
                Toast.makeText(context, R.string.success_note_add, Toast.LENGTH_SHORT).show()
                VOID.Intent1(context, CLASS.HOME)
                onBackPressed()
            }.addOnFailureListener {
                Toast.makeText(context, R.string.error_empty, Toast.LENGTH_SHORT).show()
                binding!!.progressBar.visibility = View.VISIBLE
            }
        }
    }
}