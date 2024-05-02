package com.example.forge3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(noteListPram: MutableList<NotesModel>) :RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var notesList:MutableList<NotesModel> = noteListPram
    class NoteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val noteTitle = view.findViewById<TextView>(R.id.noteTitleModel)
        val noteDescription = view.findViewById<TextView>(R.id.noteDescriptionModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemLayout = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewmodel,parent,false)
        return NoteViewHolder(itemLayout)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder){
            noteTitle.text = notesList[position].noteTitle
            noteDescription.text = notesList[position].noteDescription
        }
    }
}