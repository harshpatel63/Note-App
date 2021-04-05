package com.example.noteapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.entities.Note
import com.example.noteapp.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application){
    val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

init {
    val dao = NoteDatabase.getDatabase(application).noteDao()
    repository = NoteRepository(dao)
    allNotes = repository.allNotes
}

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun validateNote(title: String, subtitle: String, note: String): Boolean {
        if(title.isEmpty()){
            return false
        }
        if(note.isEmpty()){
            return false
        }
        return true
    }


}