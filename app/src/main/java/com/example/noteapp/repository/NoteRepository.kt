package com.example.noteapp.repository

import androidx.lifecycle.LiveData
import com.example.noteapp.dao.NoteDao
import com.example.noteapp.entities.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)
    }


}