package com.example.noteapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
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
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class CreateNoteActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, IGetUrl {

    private val REQUEST_CODE_STORAGE_PERMISSION = 12345
    private val REQUEST_CODE_IMAGE_PICK = 1245

    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var viewModel: MainViewModel
    private var selectedColor = R.color.colorDefaultNoteColor
    private var imageBitmap: Bitmap? = null
    private var webUrl: String = ""
    private var isNew = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)

        if (!intent.getBooleanExtra("isNew", true)) {
            isNew = false
            editNoteFunction()
        }

        when(intent.getStringExtra("goto")){
            "goToImage" -> createGalleryIntent()
            "goToLink" -> showAddUrlDialog()
        }

        binding.imageBack.setOnClickListener {
            onBackPressed()
            hideKeyboard()
        }


        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutMisc)

        binding.apply {
            viewColor1.setOnClickListener {
                changeTick(viewColor1)
                selectedColor = R.color.colorDefaultNoteColor
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor2.setOnClickListener {
                changeTick(viewColor2)
                selectedColor = R.color.colorNoteColor2
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor3.setOnClickListener {
                changeTick(viewColor3)
                selectedColor = R.color.colorNoteColor3
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor4.setOnClickListener {
                changeTick(viewColor4)
                selectedColor = R.color.colorNoteColor4
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }
            viewColor5.setOnClickListener {
                changeTick(viewColor5)
                selectedColor = R.color.colorNoteColor5
                viewSubtitleIndicator.setBackgroundColor(resources.getColor(selectedColor))
            }

            textMisc.setOnClickListener {
                when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            layoutAddImage.setOnClickListener {
                checkForStoragePermission()
                createGalleryIntent()
            }


            imageNote.setOnClickListener {
                binding.imageNote.setImageResource(0)
                createGalleryIntent()
            }


            imageSave.setOnClickListener {
                bindNote()
            }


            layoutAddUrl.setOnClickListener {
                showAddUrlDialog()
            }

            imageRemoveWebUrl.setOnClickListener {
                webUrl = ""
                textWebURL.text = ""
                layoutWebURL.visibility = View.GONE
                textAddLink.text = getString(R.string.add_url)
            }

            imageRemoveImage.setOnClickListener {
                imageBitmap = null
                imageNote.setImageResource(0)
                imageNote.visibility = View.GONE
                imageRemoveImage.visibility = View.GONE
                textAddImage.text = getString(R.string.add_image)
            }

            setContentView(root)
        }

    }

    private fun editNoteFunction() {
        binding.apply {
            inputNoteTitle.setText(intent.getStringExtra("editTitle").toString())
            inputNoteSubtitle.setText(intent.getStringExtra("editSubtitle").toString())
            inputNote.setText(intent.getStringExtra("editText").toString())
            selectedColor = intent.getIntExtra("editColor", R.color.colorDefaultNoteColor)

            val converters = Converters()
            val bArray = intent.getByteArrayExtra("editImage")
            bArray?.let {
                textAddImage.text = getString(R.string.change_image)
            val bitmap = converters.toBitmap(bArray)
                imageNote.setImageBitmap(bitmap)
                imageBitmap = bitmap
                imageNote.visibility = View.VISIBLE
            }

            val url: String = intent.getStringExtra("editWebLink").toString()
            if(url != ""){
                textAddLink.text = getString(R.string.edit_link)
                webUrl = url
                textWebURL.text = url
                layoutWebURL.visibility = View.VISIBLE
            }

        }
    }

    private fun showAddUrlDialog() {
        val dialogFragment = AddUrlDialogFragment(this)
        dialogFragment.show(supportFragmentManager, "getThisDone")
    }

    override fun sendUrl(url: String) {
        webUrl = url
        binding.textWebURL.text = url
        binding.textAddLink.text = getString(R.string.edit_link)
        binding.layoutWebURL.visibility = View.VISIBLE
        binding.imageRemoveWebUrl.visibility = View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        hideKeyboard()

    }

    private fun createGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_CODE_IMAGE_PICK)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            binding.imageNote.setImageURI(data.data)
            binding.imageNote.visibility = View.VISIBLE
            binding.imageRemoveImage.visibility = View.VISIBLE
            binding.textAddImage.text = getString(R.string.change_image)
            imageBitmap = (binding.imageNote.drawable as BitmapDrawable).bitmap
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun checkForStoragePermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            checkForStoragePermission()
        }
    }


    private fun changeTick(view: View) {
        binding.apply {
            when (selectedColor) {
                R.color.colorDefaultNoteColor -> imageColor1.setImageResource(0)
                R.color.colorNoteColor2 -> imageColor2.setImageResource(0)
                R.color.colorNoteColor3 -> imageColor3.setImageResource(0)
                R.color.colorNoteColor4 -> imageColor4.setImageResource(0)
                R.color.colorNoteColor5 -> imageColor5.setImageResource(0)
            }
            when (view) {
                viewColor1 -> imageColor1.setImageResource(R.drawable.ic_done)
                viewColor2 -> imageColor2.setImageResource(R.drawable.ic_done)
                viewColor3 -> imageColor3.setImageResource(R.drawable.ic_done)
                viewColor4 -> imageColor4.setImageResource(R.drawable.ic_done)
                viewColor5 -> imageColor5.setImageResource(R.drawable.ic_done)
            }
        }

    }

    private fun bindNote() {
        binding.apply {

            val title = inputNoteTitle.text.toString()
            val subtitle = inputNoteSubtitle.text.toString()


            if (viewModel.validateNote(title, subtitle)) {
                val noteText = inputNote.text.toString()
                val dateTime = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(Date())
                binding.textDateTime.text = dateTime
                val note = Note(title, dateTime, subtitle, noteText, selectedColor)

                if (!isNew) {
                    val id = intent.getIntExtra("editId", 0)
                    note.id = id
                }

                imageBitmap?.let {
                    val converters = Converters()
                    note.imagePath = converters.fromBitmap(imageBitmap!!)

                    Log.i("uhf", webUrl)
                }
                if(webUrl != ""){
                    note.webLink = webUrl
                }

                viewModel.insertNote(note)
                hideKeyboard()
                onBackPressed()
            } else {
                Snackbar.make(inputNoteTitle, "Title or Subtitle cannot be empty", Snackbar.LENGTH_SHORT).show()
            }

        }

    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}