package com.flatcode.littlenote.Unit

import com.flatcode.littlenote.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

object DATA {
    const val PARENT_PATH = "notes"
    const val CHILD_PATH = "myNotes"
    const val ID_PATH = "noteId"

    const val TITLE = "title"
    const val CONTENT = "content"
    const val COLOR = "code"
    const val DEFAULT_COLOR = 0
    const val DELAY_LOG = 2000
    const val EDIT = "Edit"
    const val DELETE = "Delete"
    const val ERR_PASS = "Password Do not Match."

    const val COLOR_OPTION = "color_option"

    val AUTH: FirebaseAuth get() = FirebaseAuth.getInstance()
    val FIREBASE_USER: FirebaseUser? get() = AUTH.currentUser
    val FirebaseUserUid: String get() = FIREBASE_USER?.uid ?: ""
    val FIREBASE_STORE: FirebaseFirestore get() = FirebaseFirestore.getInstance()

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