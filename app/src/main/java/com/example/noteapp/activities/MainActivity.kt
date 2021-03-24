package com.example.noteapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    private val REQUEST_CODE_ADD_NOTE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.notesRecyclerView.layoutManager = GridLayoutManager(this,2)
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