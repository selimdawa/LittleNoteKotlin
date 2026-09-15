package com.flatcode.littlenote.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import coil3.asImage
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
import com.flatcode.littlenote.R
import com.flatcode.littlenote.databinding.DialogAboutAccountBinding
import com.flatcode.littlenote.databinding.DialogCloseAppBinding

object VOID {

    fun closeApp(context: Context?, activity: Activity?) {
        if (context == null || activity == null) return
        val binding = DialogCloseAppBinding.inflate(activity.layoutInflater)
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            setCancelable(true)
        }

        dialog.window?.let { win ->
            win.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(win.attributes)
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            binding.yes.setOnClickListener { activity.finish() }
            binding.no.setOnClickListener { dialog.cancel() }
            dialog.show()
            win.attributes = lp
        }
    }

    fun Glide(isUser: Boolean, context: Context?, Url: String?, Image: ImageView) {
        try {
            if (Url == DATA.BASIC) {
                Image.setImageResource(if (isUser) R.drawable.ic_person else R.drawable.ic_person)
            } else {
                Image.load(Url) {
                    placeholder(ColorDrawable(ContextCompat.getColor(Image.context, R.color.image_profile)).asImage())
                    crossfade(true)
                }
            }
        } catch (e: Exception) {
            Image.setImageResource(R.drawable.ic_person)
        }
    }

    fun GlideBlur(isUser: Boolean, context: Context?, Url: String?, Image: ImageView, level: Int) {
        try {
            if (Url == DATA.BASIC) {
                Image.setImageResource(if (isUser) R.drawable.ic_person else R.drawable.ic_person)
            } else {
                Image.load(Url) {
                    placeholder(ColorDrawable(ContextCompat.getColor(Image.context, R.color.image_profile)).asImage())
                    transformations(SimpleBlurTransformation(level.toFloat()))
                }
            }
        } catch (e: Exception) {
            Image.setImageResource(R.drawable.ic_person)
        }
    }

    fun aboutAccount(context: Context?, username: String?, email: String?) {
        if (context == null) return
        val binding = DialogAboutAccountBinding.inflate((context as Activity).layoutInflater)
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            setCancelable(true)
        }

        dialog.window?.let { win ->
            win.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(win.attributes)
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            binding.username.text = username
            binding.email.text = email
            dialog.show()
            win.attributes = lp
        }
    }
}