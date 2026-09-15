package com.flatcode.littlenote.viewmodel

import androidx.lifecycle.ViewModel
import com.flatcode.littlenote.data.repository.AuthRepository
import com.flatcode.littlenote.data.repository.NoteRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    val currentUser: FirebaseUser? get() = authRepository.currentUser

    fun getNotesQuery(): Query? = currentUser?.uid?.let { noteRepository.getNotesQuery(it) }

    fun signOut() = authRepository.signOut()
}