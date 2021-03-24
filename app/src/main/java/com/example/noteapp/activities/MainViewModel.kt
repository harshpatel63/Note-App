package com.example.noteapp.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.dao.NoteDao
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application){
    lateinit var dao: NoteDao

init {
    dao = NoteDatabase.getDatabase(application).noteDao()

    fun getAllNotes(): LiveData<List<Note>>{
        return dao.getAllNotes()
    }
}

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        dao.insert(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        dao.delete(note)
    }

}