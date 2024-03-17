package com.flatcode.littlenote.Unit

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.flatcode.littlenote.Model.Note
import com.flatcode.littlenote.R

object VOID {
    fun IntentClear(context: Context, c: Class<*>?) {
        val intent = Intent(context, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
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
        val intent = Intent(context, c)
        intent.putExtra(key, value.title)
        intent.putExtra(key2, value2.content)
        intent.putExtra(key3, value3)
        intent.putExtra(key4, value4)
        context.startActivity(intent)
    }

    fun IntentExtraEditFormDetails(
        context: Context, c: Class<*>?, key: String?,
        value: Intent?, key2: String?, value2: Intent?, key3: String?, value3: Intent?
    ) {
        val intent = Intent(context, c)
        intent.putExtra(key, value!!.getStringExtra(DATA.TITLE))
        intent.putExtra(key2, value2!!.getStringExtra(DATA.CONTENT))
        intent.putExtra(key3, value3!!.getStringExtra(DATA.ID_PATH))
        context.startActivity(intent)
    }

    fun closeApp(context: Context?, activity: Activity?) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_close_app)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.findViewById<View>(R.id.yes).setOnClickListener { activity!!.finish() }
        dialog.findViewById<View>(R.id.no).setOnClickListener { dialog.cancel() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    fun aboutAccount(context: Context?, username: String?, email: String?) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_about_account)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val Username = dialog.findViewById<TextView>(R.id.username)
        Username.text = username
        val Email = dialog.findViewById<TextView>(R.id.email)
        Email.text = email
        dialog.show()
        dialog.window!!.attributes = lp
    }

    fun Intro(context: Context?, background: ImageView, backWhite: ImageView, backDark: ImageView) {
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context!!)
        if (sharedPreferences.getString("color_option", "ONE") == "ONE") {
            background.setImageResource(R.drawable.background_day)
            backWhite.visibility = View.VISIBLE
            backDark.visibility = View.GONE
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE") == "NIGHT_ONE") {
            background.setImageResource(R.drawable.background_night)
            backWhite.visibility = View.GONE
            backDark.visibility = View.VISIBLE
        }
    }

    fun Logo(context: Context?, background: ImageView) {
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context!!)
        if (sharedPreferences.getString("color_option", "ONE") == "ONE") {
            background.setImageResource(R.drawable.logo)
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE") == "NIGHT_ONE") {
            background.setImageResource(R.drawable.logo_night)
        }
    }
}