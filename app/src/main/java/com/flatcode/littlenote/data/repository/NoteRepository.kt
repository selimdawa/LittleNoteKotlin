package com.flatcode.littlenote.data.repository

import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.utils.DATA
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getNotesQuery(uid: String): Query {
        return firestore.collection(DATA.PARENT_PATH).document(uid)
            .collection(DATA.CHILD_PATH).orderBy(DATA.TITLE, Query.Direction.DESCENDING)
    }

    suspend fun addNote(uid: String, note: Note) {
        firestore.collection(DATA.PARENT_PATH).document(uid)
            .collection(DATA.CHILD_PATH).add(note).await()
    }

    suspend fun editNote(uid: String, noteId: String, note: Note) {
        firestore.collection(DATA.PARENT_PATH).document(uid)
            .collection(DATA.CHILD_PATH).document(noteId).set(note).await()
    }

    suspend fun deleteNote(uid: String, noteId: String) {
        firestore.collection(DATA.PARENT_PATH).document(uid)
            .collection(DATA.CHILD_PATH).document(noteId).delete().await()
    }
}