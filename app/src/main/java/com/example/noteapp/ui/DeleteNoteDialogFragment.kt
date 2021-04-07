package com.example.noteapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.noteapp.R

class DeleteNoteDialogFragment(private val listener: IDelete): DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.layout_delete_note, container, false)

        val cancelButton = root.findViewById<TextView>(R.id.textCancelD)
        val deleteButton = root.findViewById<TextView>(R.id.textDeleteNote)


        cancelButton.setOnClickListener {
            dismiss()
        }

        deleteButton.setOnClickListener {
            listener.deleteNoteFun()
            dismiss()
        }


        return root
    }
}
interface IDelete{
    fun deleteNoteFun()
}