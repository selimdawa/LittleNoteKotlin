package com.flatcode.littlenote.utils

import com.flatcode.littlenote.R
import com.google.firebase.FirebaseApp
import kotlin.random.Random

object DATA {
    const val PARENT_PATH = "notes"
    const val CHILD_PATH = "myNotes"
    const val NOTE = "note"

    const val COLOR = "code"
    const val DEFAULT_COLOR = 0
    const val DELAY_LOG = 2000
    const val EDIT = "Edit"
    const val DELETE = "Delete"
    const val ERR_PASS = "Password Do not Match."

    val randomColor: Int
        get() {
            val context = FirebaseApp.getInstance().applicationContext
            val typedArray = context.resources.obtainTypedArray(R.array.note_colors)
            val index = Random.nextInt(typedArray.length())
            val colorResId = typedArray.getResourceId(index, R.color.color1)
            typedArray.recycle()
            return colorResId
        }
}