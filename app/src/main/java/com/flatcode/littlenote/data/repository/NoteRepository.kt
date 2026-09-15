package com.flatcode.littlenote.data.repository

import androidx.work.*
import com.flatcode.littlenote.data.dao.NoteDao
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.data.sync.SyncWorker
import com.flatcode.littlenote.utils.DATA
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val noteDao: NoteDao,
    private val workManager: WorkManager
) {
    fun getAllNotes() = noteDao.getAllNotes()

    suspend fun addNote(uid: String, note: Note) {
        noteDao.insertNote(note.copy(isSynced = false))
        scheduleSync()
    }

    suspend fun editNote(uid: String, note: Note) {
        noteDao.updateNote(note.copy(isSynced = false))
        scheduleSync()
    }

    suspend fun deleteNote(uid: String, note: Note) {
        noteDao.deleteNote(note)
        note.remoteId?.let { remoteId ->
            try {
                firestore.collection(DATA.PARENT_PATH).document(uid)
                    .collection(DATA.CHILD_PATH).document(remoteId).delete().await()
            } catch (e: Exception) {
                Timber.e(e, "Firestore delete failed")
                // In a full implementation, we'd mark it for deletion locally and sync that too.
            }
        }
    }

    private fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "note_sync",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
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