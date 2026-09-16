package com.flatcode.littlenote.viewmodel

import androidx.lifecycle.ViewModel
import com.flatcode.littlenote.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val currentUser: FirebaseUser? get() = authRepository.currentUser

    fun signOut() = authRepository.signOut()
}