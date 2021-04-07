package com.example.noteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adapter.INoteRVAdapter
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.entities.Note

class MainActivity : AppCompatActivity(), INoteRVAdapter {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val adapter = NoteAdapter(this, this)
        binding.notesRecyclerView.adapter = adapter

        val viewModelFactory = MainViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        binding.imageAddNoteMain.setOnClickListener {
            createNoteActivityIntent("")
        }

        binding.imageAddNote.setOnClickListener {
            createNoteActivityIntent("")
        }

        binding.imageAddImage.setOnClickListener {
            createNoteActivityIntent("goToImage")
        }

        binding.imageAddWebLink.setOnClickListener {
            createNoteActivityIntent("goToLink")
        }

        viewModel.allNotes.observe(this, Observer {list ->
            list?.let {
                adapter.updateList(list)
            }

        })

        setContentView(binding.root)
    }

    private fun createNoteActivityIntent(goto: String){
        val intent = Intent(this, CreateNoteActivity::class.java)
        intent.putExtra("isNew", true)
        intent.putExtra("goto", goto)
        startActivity(intent)
    }

    override fun onNoteClicked(note: Note) {
        val editIntent = Intent(this, CreateNoteActivity::class.java)
        editIntent.putExtra("isNew", false)
        editIntent.putExtra("editId", note.id)
        editIntent.putExtra("editTitle",note.title)
        editIntent.putExtra("editSubtitle",note.subtitle)
        editIntent.putExtra("editDateTime",note.dateTime)
        editIntent.putExtra("editText",note.noteText)
        editIntent.putExtra("editColor",note.color)
        editIntent.putExtra("editWebLink",note.webLink)
        editIntent.putExtra("editImage",note.imagePath)
        startActivity(editIntent)
    }
}