package com.example.noteapp.activities

import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityCreateNoteBinding
import com.example.noteapp.entities.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var viewModel: MainViewModel
    var selectedColor = R.color.colorDefaultNoteColor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)

        binding.imageBack.setOnClickListener {
            onBackPressed()
            hideKeyboard()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutMisc)
        binding.apply {
            viewColor1.setOnClickListener{
                changeTick(viewColor1)
                selectedColor = R.color.colorDefaultNoteColor
            }
            viewColor2.setOnClickListener{
                changeTick(viewColor2)
                selectedColor = R.color.colorNoteColor2
            }
            viewColor3.setOnClickListener{
                changeTick(viewColor3)
                selectedColor = R.color.colorNoteColor3
            }
            viewColor4.setOnClickListener{
                changeTick(viewColor4)
                selectedColor = R.color.colorNoteColor4
            }
            viewColor5.setOnClickListener{
                changeTick(viewColor5)
                selectedColor = R.color.colorNoteColor5
            }
        }


        binding.imageSave.setOnClickListener {
            bindNote()
        }

        setContentView(binding.root)

    }

    private fun changeTick(view: View) {
        binding.apply {
            when(selectedColor){
                R.color.colorDefaultNoteColor -> imageColor1.setImageResource(0)
                R.color.colorNoteColor2 -> imageColor2.setImageResource(0)
                R.color.colorNoteColor3 -> imageColor3.setImageResource(0)
                R.color.colorNoteColor4 -> imageColor4.setImageResource(0)
                R.color.colorNoteColor5 -> imageColor5.setImageResource(0)
            }
            when(view){
                viewColor1 -> imageColor1.setImageResource(R.drawable.ic_done)
                viewColor2 -> imageColor2.setImageResource(R.drawable.ic_done)
                viewColor3 -> imageColor3.setImageResource(R.drawable.ic_done)
                viewColor4 -> imageColor4.setImageResource(R.drawable.ic_done)
                viewColor5 -> imageColor5.setImageResource(R.drawable.ic_done)
            }
        }

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
                val note = Note(title,dateTime,subtitle,note, selectedColor)
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