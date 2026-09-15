package com.flatcode.littlenote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.littlenote.data.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authStatus = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authStatus: StateFlow<AuthResult> = _authStatus.asStateFlow()

    val currentUser: FirebaseUser? get() = repository.currentUser

    fun signIn(email: String, password: String) {
        _authStatus.value = AuthResult.Loading
        Timber.d("Signing in with email: $email")
        viewModelScope.launch {
            try {
                repository.signIn(email, password)
                _authStatus.value = AuthResult.Success("Login Successful")
                Timber.i("Login Successful for email: $email")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Login Failed")
                Timber.e(e, "Login Failed for email: $email")
            }
        }
    }

    fun linkCredential(credential: AuthCredential, username: String) {
        _authStatus.value = AuthResult.Loading
        Timber.d("Linking credential for username: $username")
        viewModelScope.launch {
            try {
                repository.linkCredential(credential)
                repository.updateProfile(username)
                _authStatus.value = AuthResult.Success("Account Linked and Profile Updated")
                Timber.i("Account Linked and Profile Updated for username: $username")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Linking Failed")
                Timber.e(e, "Linking Failed for username: $username")
            }
        }
    }

    fun resetPassword(email: String) {
        _authStatus.value = AuthResult.Loading
        Timber.d("Sending password reset email to: $email")
        viewModelScope.launch {
            try {
                repository.sendPasswordResetEmail(email)
                _authStatus.value = AuthResult.Success("Reset email sent to $email")
                Timber.i("Reset email sent to $email")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Failed to send reset email")
                Timber.e(e, "Failed to send reset email to $email")
            }
        }
    }

    fun deleteAnonymousUser(uid: String) {
        Timber.d("Deleting anonymous user: $uid")
        viewModelScope.launch {
            try {
                repository.deleteUserNotes(uid)
                repository.deleteUser()
                _authStatus.value = AuthResult.Success("User deleted")
                Timber.i("Anonymous user deleted: $uid")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Deletion Failed")
                Timber.e(e, "Deletion Failed for anonymous user: $uid")
            }
        }
    }

    fun signInAnonymously() {
        _authStatus.value = AuthResult.Loading
        Timber.d("Signing in anonymously")
        viewModelScope.launch {
            try {
                repository.signInAnonymously()
                _authStatus.value = AuthResult.Success("Anonymous Login Successful")
                Timber.i("Anonymous Login Successful")
            } catch (e: Exception) {
                _authStatus.value = AuthResult.Error(e.message ?: "Anonymous Login Failed")
                Timber.e(e, "Anonymous Login Failed")
            }
        }
    }

    fun checkUserAndRedirect(delay: Long) {
        viewModelScope.launch {
            delay(delay)
            if (currentUser != null) {
                _authStatus.value = AuthResult.Authenticated
            } else {
                signInAnonymously()
            }
        }
    }

    fun signOut() = repository.signOut().also { Timber.d("Signing out") }

    sealed class AuthResult {
        object Idle : AuthResult()
        object Loading : AuthResult()
        object Authenticated : AuthResult()
        data class Success(val message: String) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }
}