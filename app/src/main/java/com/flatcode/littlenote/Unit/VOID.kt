package com.flatcode.littlenote.Unit

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.flatcode.littlenote.Model.Note
import com.flatcode.littlenote.R

object VOID {

    fun IntentClear(context: Context, c: Class<*>?) {
        val intent = Intent(context, c).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun Intent1(context: Context, c: Class<*>?) {
        val intent = Intent(context, c)
        context.startActivity(intent)
    }

    fun IntentExtraDetails(
        context: Context, c: Class<*>?, key: String?, value: Note, key2: String?, value2: Note,
        key3: String?, value3: Int, key4: String?, value4: String?
    ) {
        val intent = Intent(context, c).apply {
            putExtra(key, value.title)
            putExtra(key2, value2.content)
            putExtra(key3, value3)
            putExtra(key4, value4)
        }
        context.startActivity(intent)
    }

    fun IntentExtraEditFormDetails(
        context: Context, c: Class<*>?, key: String?,
        value: Intent?, key2: String?, value2: Intent?, key3: String?, value3: Intent?
    ) {
        val intent = Intent(context, c).apply {
            putExtra(key, value?.getStringExtra(DATA.TITLE))
            putExtra(key2, value2?.getStringExtra(DATA.CONTENT))
            putExtra(key3, value3?.getStringExtra(DATA.ID_PATH))
        }
        context.startActivity(intent)
    }

    fun closeApp(context: Context?, activity: Activity?) {
        if (context == null || activity == null) return
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_close_app)
            setCancelable(true)
        }

        dialog.window?.let { win ->
            win.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(win.attributes)
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            dialog.findViewById<TextView>(R.id.yes)?.setOnClickListener { activity.finish() }
            dialog.findViewById<TextView>(R.id.no)?.setOnClickListener { dialog.cancel() }
            dialog.show()
            win.attributes = lp
        }
    }

    fun aboutAccount(context: Context?, username: String?, email: String?) {
        if (context == null) return
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_about_account)
            setCancelable(true)
        }

        dialog.window?.let { win ->
            win.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(win.attributes)
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            dialog.findViewById<TextView>(R.id.username)?.text = username
            dialog.findViewById<TextView>(R.id.email)?.text = email
            dialog.show()
            win.attributes = lp
        }
    }
}