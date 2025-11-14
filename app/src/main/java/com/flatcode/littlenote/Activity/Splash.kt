package com.flatcode.littlenote.Activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    var auth: FirebaseAuth? = null
    private val context: Context = this@Splash

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        VOID.Logo(baseContext, binding!!.logo)
        VOID.Intro(baseContext, binding!!.background, binding!!.backWhite, binding!!.backBlack)

        auth = FirebaseAuth.getInstance()
        val handler = Handler()
        handler.postDelayed({
            // check if user is logged in
            if (auth!!.currentUser != null) {
                VOID.Intent1(context, CLASS.HOME)
                finish()
            } else {
                // create new anonymous account
                auth!!.signInAnonymously().addOnSuccessListener {
                    Toast.makeText(context, R.string.temporary_log, Toast.LENGTH_LONG).show()
                    VOID.Intent1(context, CLASS.HOME)
                    finish()
                }.addOnFailureListener { e: Exception ->
                    Toast.makeText(
                        context, R.string.error_log.toString() + e.message, Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }, DATA.DELAY_LOG.toLong())
    }
}