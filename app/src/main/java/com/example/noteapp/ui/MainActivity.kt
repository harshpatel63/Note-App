package com.example.noteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    private val REQUEST_CODE_ADD_NOTE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val adapter = NoteAdapter(this)
        binding.notesRecyclerView.adapter = adapter

        val viewModelFactory = MainViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        binding.imageAddNoteMain.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, CreateNoteActivity::class.java)
                ,REQUEST_CODE_ADD_NOTE
            )
        }
        viewModel.allNotes.observe(this, Observer {list ->
            list?.let {
                adapter.updateList(list)
            }

        })

        setContentView(binding.root)
    }
}