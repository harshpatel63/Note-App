package com.example.noteapp.activities

import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityCreateNoteBinding
import com.example.noteapp.entities.Note
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    lateinit var viewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)

        binding.imageBack.setOnClickListener {
            onBackPressed()
            hideKeyboard()
        }

        binding.imageSave.setOnClickListener {
            bindNote()
        }

        setContentView(binding.root)

    }

    private fun bindNote(){
        binding.apply {
            val title = inputNoteTitle.text.toString()
            val subtitle = inputNoteSubtitle.text.toString()
            val note = inputNote.text.toString()
            val dateTime = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                    .format(Date())
            binding.textDateTime.text = dateTime

            if(viewModel.validateNote(title, subtitle, note)){
                val note = Note(title,dateTime,subtitle,note, R.color.colorNoteColor2)
                viewModel.insertNote(note)
                hideKeyboard()
                onBackPressed()
            }
            else
            {
                // Implement for empty title and subtitle
            }

        }

    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



}