package com.example.noteapp.entities

import android.graphics.Bitmap
import android.hardware.biometrics.BiometricManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

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
        val color: Int
){
        @PrimaryKey(autoGenerate = true)
        var id = 0

        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        var imagePath: ByteArray? = null

        @ColumnInfo(name = "web_link")
        var webLink: String = ""
}