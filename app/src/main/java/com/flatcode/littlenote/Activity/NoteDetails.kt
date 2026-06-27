package com.flatcode.littlenote.Activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityNoteDetailsBinding

class NoteDetails : AppCompatActivity() {

    private var _binding: ActivityNoteDetailsBinding? = null
    private val binding get() = _binding!!
    private val context: Context = this@NoteDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                applyTransition()
                finish()
            }
        })

        val data = intent

        binding.toolbar.nameSpace.text = data.getStringExtra(DATA.TITLE)
        binding.description.movementMethod = ScrollingMovementMethod()
        binding.description.text = data.getStringExtra(DATA.CONTENT)

        val colorRes = data.getIntExtra(DATA.COLOR, DATA.DEFAULT_COLOR)
        binding.description.setBackgroundColor(ContextCompat.getColor(context, colorRes))

        binding.toolbar.edit.setOnClickListener {
            VOID.IntentExtraEditFormDetails(
                context, CLASS.EDIT, DATA.TITLE, data, DATA.CONTENT, data, DATA.ID_PATH, data
            )
            applyTransition()
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