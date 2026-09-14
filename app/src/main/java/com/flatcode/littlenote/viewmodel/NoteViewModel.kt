package com.flatcode.littlenote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.data.repository.AuthRepository
import com.flatcode.littlenote.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _noteStatus = MutableStateFlow<NoteResult>(NoteResult.Idle)
    val noteStatus: StateFlow<NoteResult> = _noteStatus.asStateFlow()

    fun addNote(title: String, content: String) {
        val uid = authRepository.currentUser?.uid ?: return
        val note = Note(title, content)
        _noteStatus.value = NoteResult.Loading
        Timber.d("Adding note: title=$title")
        viewModelScope.launch {
            try {
                noteRepository.addNote(uid, note)
                _noteStatus.value = NoteResult.Success("Note Added Successfully")
                Timber.i("Note Added Successfully: $title")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to add note")
                Timber.e(e, "Failed to add note: $title")
            }
        }
    }

    fun editNote(noteId: String, title: String, content: String) {
        val uid = authRepository.currentUser?.uid ?: return
        val note = Note(title, content)
        _noteStatus.value = NoteResult.Loading
        Timber.d("Editing note: id=$noteId, title=$title")
        viewModelScope.launch {
            try {
                noteRepository.editNote(uid, noteId, note)
                _noteStatus.value = NoteResult.Success("Note Updated Successfully")
                Timber.i("Note Updated Successfully: $noteId")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to update note")
                Timber.e(e, "Failed to update note: $noteId")
            }
        }
    }

    fun deleteNote(noteId: String) {
        val uid = authRepository.currentUser?.uid ?: return
        Timber.d("Deleting note: id=$noteId")
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(uid, noteId)
                _noteStatus.value = NoteResult.Success("Note Deleted Successfully")
                Timber.i("Note Deleted Successfully: $noteId")
            } catch (e: Exception) {
                _noteStatus.value = NoteResult.Error(e.message ?: "Failed to delete note")
                Timber.e(e, "Failed to delete note: $noteId")
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