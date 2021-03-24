package com.example.noteapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    private val REQUEST_CODE_ADD_NOTE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val viewModelFactory = MainViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        binding.imageAddNoteMain.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, CreateNoteActivity::class.java)
                ,REQUEST_CODE_ADD_NOTE
            )
        }

        setContentView(binding.root)
    }
}