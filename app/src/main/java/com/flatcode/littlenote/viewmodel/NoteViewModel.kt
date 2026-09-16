package com.flatcode.littlenote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.data.repository.AuthRepository
import com.flatcode.littlenote.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val authRepository: AuthRepository, private val noteRepository: NoteRepository
) : ViewModel() {

    private val _noteStatus = MutableStateFlow<NoteResult>(NoteResult.Idle)
    val noteStatus: StateFlow<NoteResult> = _noteStatus.asStateFlow()

    val allNotes: StateFlow<List<Note>> =
        noteRepository.getAllNotes().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNote(title: String, content: String) {
        authRepository.currentUser?.uid ?: return
        val note = Note(title = title, content = content)
        _noteStatus.value = NoteResult.Loading
        viewModelScope.launch {
            try {
                noteRepository.addNote(note)
                _noteStatus.value = NoteResult.Success("Note Added Successfully")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to add note")
            }
        }
    }

    fun editNote(note: Note) {
        authRepository.currentUser?.uid ?: return
        _noteStatus.value = NoteResult.Loading
        viewModelScope.launch {
            try {
                noteRepository.editNote(note)
                _noteStatus.value = NoteResult.Success("Note Updated Successfully")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to update note")
            }
        }
    }

    fun deleteNote(note: Note) {
        val uid = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(uid, note)
                _noteStatus.value = NoteResult.Success("Note Deleted Successfully")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to delete note")
            }
        }
    }

    fun syncNotes() {
        val uid = authRepository.currentUser?.uid ?: return
        _noteStatus.value = NoteResult.Loading
        viewModelScope.launch {
            try {
                noteRepository.syncWithFirestore(uid)
                _noteStatus.value = NoteResult.Success("Sync Completed")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Sync Failed")
            }
        }
    }

    sealed class NoteResult {
        object Idle : NoteResult()
        object Loading : NoteResult()
        data class Success(val message: String) : NoteResult()
        data class Error(val message: String) : NoteResult()
    }
}