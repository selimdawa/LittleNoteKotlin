package com.flatcode.littlenote.data.repository

import com.flatcode.littlenote.data.dao.NoteDao
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.utils.DATA
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val noteDao: NoteDao
) {
    fun getAllNotes() = noteDao.getAllNotes()

    suspend fun addNote(uid: String, note: Note) {
        val id = noteDao.insertNote(note)
        val insertedNote = note.copy(id = id.toInt())
        try {
            val docRef = firestore.collection(DATA.PARENT_PATH).document(uid)
                .collection(DATA.CHILD_PATH).add(insertedNote).await()
            noteDao.updateNote(insertedNote.copy(remoteId = docRef.id))
        } catch (e: Exception) {
            Timber.e(e, "Firestore add failed")
        }
    }

    suspend fun editNote(uid: String, note: Note) {
        noteDao.updateNote(note)
        note.remoteId?.let { remoteId ->
            try {
                firestore.collection(DATA.PARENT_PATH).document(uid)
                    .collection(DATA.CHILD_PATH).document(remoteId).set(note).await()
            } catch (e: Exception) {
                Timber.e(e, "Firestore edit failed")
            }
        }
    }

    suspend fun deleteNote(uid: String, note: Note) {
        noteDao.deleteNote(note)
        note.remoteId?.let { remoteId ->
            try {
                firestore.collection(DATA.PARENT_PATH).document(uid)
                    .collection(DATA.CHILD_PATH).document(remoteId).delete().await()
            } catch (e: Exception) {
                Timber.e(e, "Firestore delete failed")
            }
        }
    }

    suspend fun syncWithFirestore(uid: String) {
        try {
            val snapshot = firestore.collection(DATA.PARENT_PATH).document(uid)
                .collection(DATA.CHILD_PATH).get().await()
            val remoteNotes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Note::class.java)?.apply {
                    remoteId = doc.id
                }
            }
            remoteNotes.forEach { remoteNote ->
                val localNote = remoteNote.remoteId?.let { noteDao.getNoteByRemoteId(it) }
                if (localNote != null) {
                    noteDao.updateNote(remoteNote.copy(id = localNote.id))
                } else {
                    noteDao.insertNote(remoteNote)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Sync failed")
        }
    }
}