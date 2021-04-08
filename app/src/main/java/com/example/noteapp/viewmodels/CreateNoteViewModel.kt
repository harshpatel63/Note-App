package com.example.noteapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.R
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.entities.Note
import com.example.noteapp.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateNoteViewModel(application: Application): AndroidViewModel(application) {


    private val _selectedColor = MutableLiveData<Int>()
    val selectedColor : LiveData<Int>
    get() = _selectedColor

    private val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun validateNote(title: String, subtitle: String): Boolean {
        if(title.isEmpty()){
            return false
        }
        if(subtitle.isEmpty()){
            return false
        }
        return true
    }

    fun changeColor(color: Int){
        Log.i("sjldfh", color.toString())
        _selectedColor.value = color
    }



}