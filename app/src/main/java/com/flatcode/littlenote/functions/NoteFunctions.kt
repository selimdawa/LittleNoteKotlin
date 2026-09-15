package com.flatcode.littlenote.functions

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.AppFunction
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.data.repository.AuthRepository
import com.flatcode.littlenote.data.repository.NoteRepository
import javax.inject.Inject

/**
 * A class containing AppFunctions for managing notes.
 */
class NoteFunctions @Inject constructor(
    private val noteRepository: NoteRepository,
    private val authRepository: AuthRepository
) {
    /**
     * Adds a new note to the app.
     *
     * @param appFunctionContext The execution context.
     * @param title The title of the note.
     * @param content The content of the note.
     * @return A message indicating the result of the operation.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun addNote(
        appFunctionContext: AppFunctionContext,
        title: String,
        content: String
    ): String {
        val uid = authRepository.currentUser?.uid ?: return "User not logged in"
        val note = Note(title = title, content = content)
        noteRepository.addNote(uid, note)
        return "Note added successfully"
    }
}