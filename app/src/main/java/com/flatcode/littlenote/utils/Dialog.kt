package com.flatcode.littlenote.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.flatcode.littlenote.databinding.DialogAboutAccountBinding
import com.flatcode.littlenote.databinding.DialogCloseAppBinding
import com.flatcode.littlenote.databinding.DialogDeleteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.dialogCloseApp() {
    val activity = this as? Activity ?: return
    if (activity.isFinishing || activity.isDestroyed) return

    val dialogBinding = DialogCloseAppBinding.inflate(LayoutInflater.from(this))
    val alertDialog = MaterialAlertDialogBuilder(this).setView(dialogBinding.root).create()

    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    dialogBinding.yes.setOnClickListener {
        activity.finish()
    }

    dialogBinding.no.setOnClickListener {
        alertDialog.dismiss()
    }

    alertDialog.show()

    val widthPx = (300 * resources.displayMetrics.density).toInt()
    alertDialog.window?.setLayout(widthPx, ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun Context.dialogAboutAccount(username: String?, email: String?) {
    val activity = this as? Activity
    if (activity != null && (activity.isFinishing || activity.isDestroyed)) return

    val dialogBinding = DialogAboutAccountBinding.inflate(LayoutInflater.from(this))
    val alertDialog = MaterialAlertDialogBuilder(this).setView(dialogBinding.root).create()

    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    dialogBinding.username.text = username
    dialogBinding.email.text = email

    alertDialog.show()

    val widthPx = (300 * resources.displayMetrics.density).toInt()
    alertDialog.window?.setLayout(widthPx, ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun Context.dialogDelete(onSync: () -> Unit, onLogout: () -> Unit) {
    val activity = this as? Activity
    if (activity != null && (activity.isFinishing || activity.isDestroyed)) return

    val dialogBinding = DialogDeleteBinding.inflate(LayoutInflater.from(this))
    val alertDialog = MaterialAlertDialogBuilder(this).setView(dialogBinding.root).create()

    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    dialogBinding.sync.setOnClickListener {
        onSync()
        alertDialog.dismiss()
    }

    dialogBinding.logout.setOnClickListener {
        onLogout()
        alertDialog.dismiss()
    }

    alertDialog.show()

    val widthPx = (300 * resources.displayMetrics.density).toInt()
    alertDialog.window?.setLayout(widthPx, ViewGroup.LayoutParams.WRAP_CONTENT)
}