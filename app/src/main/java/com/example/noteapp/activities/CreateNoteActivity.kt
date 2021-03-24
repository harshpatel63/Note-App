package com.example.noteapp.activities

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Database
import com.example.noteapp.R
import com.example.noteapp.dao.NoteDao
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.databinding.ActivityCreateNoteBinding
import com.example.noteapp.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding

    private lateinit var dao: NoteDao



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)

        dao = NoteDatabase.getDatabase(this).noteDao()


        binding.textDateTime.setText(
           SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(Date())
        )


        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            bindNote()
        }

        setContentView(binding.root)

    }

    private fun bindNote() {
        binding.apply {
            val title = inputNoteTitle.text.toString()
            val subtitle = inputNoteSubtitle.text.toString()
            val note = inputNote.text.toString()
            if(validateNote(title, subtitle, note)){
                val note = Note(title,"",subtitle,note, "${R.color.colorNoteColor2}")
                insertNote(note)
                onBackPressed()
            }

        }

    }

    private fun insertNote(note: Note) = GlobalScope.launch(Dispatchers.IO) {
        dao.insert(note)
    }

    private fun validateNote(title: String, subtitle: String, note: String): Boolean {
        if(title.isEmpty()){
            Toast.makeText(this,"Title must not be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if(note.isEmpty()){
            Toast.makeText(this,"Note must not be empty",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}