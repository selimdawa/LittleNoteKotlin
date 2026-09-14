package com.flatcode.littlenote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.data.repository.AuthRepository
import com.flatcode.littlenote.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _noteStatus = MutableLiveData<NoteResult>()
    val noteStatus: LiveData<NoteResult> = _noteStatus

    fun addNote(title: String, content: String) {
        val uid = authRepository.currentUser?.uid ?: return
        val note = Note(title, content)
        _noteStatus.value = NoteResult.Loading
        viewModelScope.launch {
            try {
                noteRepository.addNote(uid, note)
                _noteStatus.value = NoteResult.Success("Note Added Successfully")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to add note")
            }
        }
    }

    fun editNote(noteId: String, title: String, content: String) {
        val uid = authRepository.currentUser?.uid ?: return
        val note = Note(title, content)
        _noteStatus.value = NoteResult.Loading
        viewModelScope.launch {
            try {
                noteRepository.editNote(uid, noteId, note)
                _noteStatus.value = NoteResult.Success("Note Updated Successfully")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to update note")
            }
        }
    }

    fun deleteNote(noteId: String) {
        val uid = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(uid, noteId)
                _noteStatus.value = NoteResult.Success("Note Deleted Successfully")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to delete note")
            }
        }
    }

    sealed class NoteResult {
        object Loading : NoteResult()
        data class Success(val message: String) : NoteResult()
        data class Error(val message: String) : NoteResult()
    }
}