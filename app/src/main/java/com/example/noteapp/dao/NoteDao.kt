package com.example.noteapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noteapp.entities.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes_table order by id DESC")
    fun getAllNotes(): LiveData<List<Note>>
}