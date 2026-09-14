package com.flatcode.littlenote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.littlenote.data.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authStatus = MutableLiveData<AuthResult>()
    val authStatus: LiveData<AuthResult> = _authStatus

    val currentUser: FirebaseUser? get() = repository.currentUser

    fun signIn(email: String, password: String) {
        _authStatus.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                repository.signIn(email, password)
                _authStatus.value = AuthResult.Success("Login Successful")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Login Failed")
            }
        }
    }

    fun linkCredential(credential: AuthCredential, username: String) {
        _authStatus.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                repository.linkCredential(credential)
                repository.updateProfile(username)
                _authStatus.value = AuthResult.Success("Account Linked and Profile Updated")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Linking Failed")
            }
        }
    }

    fun resetPassword(email: String) {
        _authStatus.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                repository.sendPasswordResetEmail(email)
                _authStatus.value = AuthResult.Success("Reset email sent to $email")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Failed to send reset email")
            }
        }
    }

    fun deleteAnonymousUser(uid: String) {
        viewModelScope.launch {
            try {
                repository.deleteUserNotes(uid)
                repository.deleteUser()
                _authStatus.value = AuthResult.Success("User deleted")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Deletion Failed")
            }
        }
    }

    fun signInAnonymously() {
        _authStatus.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                repository.signInAnonymously()
                _authStatus.value = AuthResult.Success("Anonymous Login Successful")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Anonymous Login Failed")
            }
        }
    }

    fun signOut() {
        repository.signOut()
    }

    sealed class AuthResult {
        object Loading : AuthResult()
        data class Success(val message: String) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }
}