package com.flatcode.littlenote.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityNoteDetailsBinding

class NoteDetails : AppCompatActivity() {

    private var binding: ActivityNoteDetailsBinding? = null
    var data: Intent? = null
    private val context: Context = this@NoteDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        data = intent

        binding!!.toolbar.nameSpace.text = data!!.getStringExtra(DATA.TITLE)
        binding!!.description.movementMethod = ScrollingMovementMethod()
        binding!!.description.text = data!!.getStringExtra(DATA.CONTENT)
        binding!!.description.setBackgroundColor(
            resources.getColor(data!!.getIntExtra(DATA.COLOR, DATA.DEFAULT_COLOR), null)
        )
        binding!!.toolbar.edit.setOnClickListener {
            VOID.IntentExtraEditFormDetails(
                context, CLASS.EDIT, DATA.TITLE, data, DATA.CONTENT, data, DATA.ID_PATH, data
            )
        }
    }
}