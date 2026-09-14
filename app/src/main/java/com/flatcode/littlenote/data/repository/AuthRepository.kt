package com.flatcode.littlenote.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun signIn(email: String, password: String) = auth.signInWithEmailAndPassword(email, password).await()

    suspend fun linkCredential(credential: AuthCredential) = auth.currentUser?.linkWithCredential(credential)?.await()

    suspend fun updateProfile(username: String) {
        val request = UserProfileChangeRequest.Builder().setDisplayName(username).build()
        auth.currentUser?.updateProfile(request)?.await()
    }

    suspend fun deleteUserNotes(uid: String) {
        firestore.collection("notes").document(uid).delete().await()
    }

    suspend fun deleteUser() {
        auth.currentUser?.delete()?.await()
    }

    fun signOut() = auth.signOut()

    suspend fun sendPasswordResetEmail(email: String) = auth.sendPasswordResetEmail(email).await()

    suspend fun signInAnonymously() = auth.signInAnonymously().await()
}