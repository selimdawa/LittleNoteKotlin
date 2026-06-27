package com.flatcode.littlenote.Activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private val context: Context = this@Splash

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = DATA.FIREBASE_USER
            if (currentUser != null) {
                VOID.Intent1(context, CLASS.HOME)
                applyTransition()
                finish()
            } else {
                DATA.AUTH.signInAnonymously().addOnSuccessListener {
                    Toast.makeText(context, R.string.temporary_log, Toast.LENGTH_LONG).show()
                    VOID.Intent1(context, CLASS.HOME)
                    applyTransition()
                    finish()
                }.addOnFailureListener { e: Exception ->
                    Toast.makeText(context, "${getString(R.string.error_log)}${e.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }, DATA.DELAY_LOG.toLong())
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