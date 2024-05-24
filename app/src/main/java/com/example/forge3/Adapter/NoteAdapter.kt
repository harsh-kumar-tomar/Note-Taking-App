package com.example.forge3.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forge3.Activity.MakeNoteActivity
import com.example.forge3.Model.NotesModel
import com.example.forge3.R

class NoteAdapter(val notesList: MutableList<NotesModel>) :RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle = view.findViewById<TextView>(R.id.noteTitleModel)
        val noteDescription = view.findViewById<TextView>(R.id.noteDescriptionModel)
//        init {
//            view.setOnClickListener {
//                val position = adapterPosition
//                val intent = Intent(view.context, MakeNoteActivity::class.java)
//                intent.putExtra("noteTitle", noteTitle.text)
//                intent.putExtra("noteDescription", noteDescription.text)
//                view.context.startActivity(intent)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewmodel, parent, false)
        return NoteViewHolder(itemLayout)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            noteTitle.text = notesList[position].noteTitle
            noteDescription.text = notesList[position].noteDescription

        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MakeNoteActivity::class.java).apply {
                putExtra("noteTitle", notesList[position].noteTitle)
                putExtra("noteDescription", notesList[position].noteDescription)
            }
            holder.itemView.context.startActivity(intent)

        }


    }
    fun updateNotes(newNotes: List<NotesModel>) {
        notesList.clear()
        notesList.addAll(newNotes)
        notifyDataSetChanged()
    }
}