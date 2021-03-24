package com.example.noteapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.entities.Note

class NoteAdapter(private  val context: Context): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    val allNotes = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemTitle = itemView.findViewById<TextView>(R.id.itemTitle)
        val itemSubtitle = itemView.findViewById<TextView>(R.id.itemSubtitle)
        val itemNote = itemView.findViewById<TextView>(R.id.itemNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_itemview,parent,false))
        return viewHolder
    }

    override fun getItemCount() = allNotes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = allNotes[position]
        holder.itemTitle.text = currentNote.title
        holder.itemSubtitle.text = currentNote.subtitle
        holder.itemNote.text = currentNote.noteText
        holder.itemView.setBackgroundColor(currentNote.color)
    }
    fun updateList(newList: List<Note>){
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

}