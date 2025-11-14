package com.flatcode.littlenote.Activity

import android.content.Context
import android.content.Intent
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

class EditNote : AppCompatActivity() {

    private var binding: ActivityAddEditNoteBinding? = null
    var data: Intent? = null
    private val context: Context = this@EditNote

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        data = intent
        val noteTitle = data!!.getStringExtra(DATA.TITLE)
        val noteContent = data!!.getStringExtra(DATA.CONTENT)

        binding!!.toolbar.nameSpace.setText(R.string.edit_note)
        binding!!.toolbar.image.visibility = View.VISIBLE
        binding!!.toolbar.image.setImageResource(R.drawable.ic_true)
        binding!!.noteTitle.setText(noteTitle)
        binding!!.noteContent.setText(noteContent)
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
                    .collection(DATA.CHILD_PATH).document(data!!.getStringExtra(DATA.ID_PATH)!!)
            val note: MutableMap<String?, Any> = HashMap()
            note[DATA.TITLE] = nTitle
            note[DATA.CONTENT] = nContent
            document.update(note).addOnSuccessListener {
                Toast.makeText(context, R.string.note_saved, Toast.LENGTH_SHORT).show()
                VOID.Intent1(context, CLASS.HOME)
                onBackPressed()
            }.addOnFailureListener {
                Toast.makeText(context, R.string.error_saved, Toast.LENGTH_SHORT).show()
                binding!!.progressBar.visibility = View.VISIBLE
            }
        }
    }
}