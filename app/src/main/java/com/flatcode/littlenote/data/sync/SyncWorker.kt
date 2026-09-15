package com.flatcode.littlenote.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.flatcode.littlenote.data.dao.NoteDao
import com.flatcode.littlenote.utils.DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val noteDao: NoteDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val uid = auth.currentUser?.uid ?: return Result.failure()
        val unsyncedNotes = noteDao.getUnsyncedNotes()

        if (unsyncedNotes.isEmpty()) return Result.success()

        return try {
            unsyncedNotes.forEach { note ->
                if (note.remoteId == null) {
                    // New Note
                    val docRef = firestore.collection(DATA.PARENT_PATH).document(uid)
                        .collection(DATA.CHILD_PATH).add(note).await()
                    noteDao.updateNote(note.copy(remoteId = docRef.id, isSynced = true))
                } else {
                    // Update Note
                    firestore.collection(DATA.PARENT_PATH).document(uid)
                        .collection(DATA.CHILD_PATH).document(note.remoteId!!).set(note).await()
                    noteDao.updateNote(note.copy(isSynced = true))
                }
            }
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "SyncWorker failed")
            Result.retry()
        }
    }
}