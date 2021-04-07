package com.example.noteapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.Converters
import com.example.noteapp.R
import com.example.noteapp.entities.Note
import com.makeramen.roundedimageview.RoundedImageView

class NoteAdapter(private  val context: Context, private val listener: INoteRVAdapter): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    val allNotes = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemTitle = itemView.findViewById<TextView>(R.id.itemTitle)
        val itemSubtitle = itemView.findViewById<TextView>(R.id.itemSubtitle)
        val layoutNote = itemView.findViewById<ConstraintLayout>(R.id.layoutNote)
        val itemImage = itemView.findViewById<RoundedImageView>(R.id.itemImage)
        val itemDateTime = itemView.findViewById<TextView>(R.id.itemDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_itemview,parent,false)
        val viewHolder = NoteViewHolder(view)
        view.setOnClickListener {
            listener.onNoteClicked(allNotes[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount() = allNotes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = allNotes[position]
        holder.itemTitle.text = currentNote.title
        holder.itemSubtitle.text = currentNote.subtitle
        holder.itemDateTime.text = currentNote.dateTime
        holder.layoutNote.setBackgroundResource(currentNote.color)
        currentNote.imagePath?.let {
            val converters = Converters()
            holder.itemImage.setImageBitmap(converters.toBitmap(currentNote.imagePath!!))
            holder.itemImage.visibility = View.VISIBLE
        }


    }
    fun updateList(newList: List<Note>){
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

}

interface INoteRVAdapter{
    fun onNoteClicked(note: Note)
}