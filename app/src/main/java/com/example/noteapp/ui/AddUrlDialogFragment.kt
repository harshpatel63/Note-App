package com.example.noteapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.example.noteapp.R

class AddUrlDialogFragment(private val listener: IGetUrl) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.layout_add_url, container, false)

        val addButton = root.findViewById<TextView>(R.id.textAdd)
        val cancelButton = root.findViewById<TextView>(R.id.textCancel)
        val urlEditText: EditText = root.findViewById(R.id.inputURL)
        addButton.setOnClickListener {
            val url = urlEditText.text.toString()
            if(url != ""){
                listener.sendUrl(url)
                dismiss()
            } else
            {
                urlEditText.error = "Enter URL"
            }
        }
        cancelButton.setOnClickListener {
            dismiss()
        }

        return root
    }

}

interface IGetUrl {
    fun sendUrl(url : String)
}