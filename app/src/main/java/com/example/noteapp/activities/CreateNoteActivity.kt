package com.example.noteapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.Converters
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityCreateNoteBinding
import com.example.noteapp.entities.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class CreateNoteActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val REQUEST_CODE_STORAGE_PERMISSION = 12345
    private val REQUEST_CODE_IMAGE_PICK = 1245

    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var viewModel: MainViewModel
    private var selectedColor = R.color.colorDefaultNoteColor
     private var imageBitmap: Bitmap? = null


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
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor2.setOnClickListener{
                changeTick(viewColor2)
                selectedColor = R.color.colorNoteColor2
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor3.setOnClickListener{
                changeTick(viewColor3)
                selectedColor = R.color.colorNoteColor3
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor4.setOnClickListener{
                changeTick(viewColor4)
                selectedColor = R.color.colorNoteColor4
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor5.setOnClickListener{
                changeTick(viewColor5)
                selectedColor = R.color.colorNoteColor5
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
        }
        binding.textMisc.setOnClickListener {
        when(bottomSheetBehavior.state){
            BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        }

        binding.layoutAddImage.setOnClickListener {
                checkForStoragePermission()
                createGalleryIntent()
        }

        binding.imageNote.setOnClickListener {
            binding.imageNote.setImageResource(0)
            createGalleryIntent()
        }


        binding.imageSave.setOnClickListener {
            bindNote()
        }

        setContentView(binding.root)

    }

    private fun createGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setType("image/*")
        startActivityForResult(galleryIntent, REQUEST_CODE_IMAGE_PICK)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data!=null){
            binding.imageNote.setImageURI(data.data)
            binding.imageNote.visibility = View.VISIBLE
            imageBitmap = (binding.imageNote.drawable as BitmapDrawable).bitmap
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun checkForStoragePermission() {
        if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            return
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept Read External Storage permission to add image",
                    REQUEST_CODE_STORAGE_PERMISSION,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        } else {
            checkForStoragePermission()
        }
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
            val noteText = inputNote.text.toString()
            val dateTime = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                    .format(Date())
            binding.textDateTime.text = dateTime

            if(viewModel.validateNote(title, subtitle, noteText)){
                val note = Note(title, dateTime, subtitle, noteText, selectedColor)
                imageBitmap?.let {
                    val converters = Converters()
                    note.imagePath = converters.fromBitmap(imageBitmap!!)
                }

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