package com.example.noteapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Note(
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "date_time")
        val dateTime: String,
        @ColumnInfo(name = "subtitle")
        val subtitle: String,
        @ColumnInfo(name = "note_text")
        val noteText: String,
        @ColumnInfo(name = "color")
        val color: String?
){
        @PrimaryKey(autoGenerate = true)
        var id = 0

        @ColumnInfo(name = "image_path")
        var imagePath: String = ""

        @ColumnInfo(name = "web_link")
        var webLink: String = ""
}