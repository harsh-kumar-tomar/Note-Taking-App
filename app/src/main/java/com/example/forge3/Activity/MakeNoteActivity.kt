package com.example.forge3.Activity
import android.content.ClipDescription
import com.example.forge3.databinding.ActivityMakeNoteBinding
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.forge3.Model.NotesModel
import com.example.forge3.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MakeNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMakeNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_make_note)


        val  intent = intent
        val noteTitle = intent.getStringExtra("noteTitle")
        val noteDescription = intent.getStringExtra("noteDescription")

        //intent opening a note
        if (noteTitle!=null && noteDescription!=null)
        {
            binding.noteTitle.setText(noteTitle ,TextView.BufferType.EDITABLE)
            binding.noteDescription.setText(noteDescription ,TextView.BufferType.EDITABLE)
        }


        binding.materialToolbar.setNavigationOnClickListener {
            save()
        }

        //setting date and time
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd.MM, hh:mma", Locale.getDefault())
        val formattedDateTime = dateFormat.format(currentDateTime)
        binding.dateTime.text = formattedDateTime


    }

//    fun save(title:String , description:String)

    //save btn onclick
    fun save() {
        if (binding.noteTitle.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Empty Title", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.noteDescription.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Empty Note", Toast.LENGTH_SHORT).show()
            return
        }

        Thread { saveDataToFirebase() }.start()
        finish()
    }


    private fun saveDataToFirebase() {

        //making a note
        val note = NotesModel(binding.noteTitle.text.toString() , binding.noteDescription.text.toString())
        val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance() // reference to root node

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        val reference: DatabaseReference = rootNode.getReference("").child("Users").child(userId!!).child("notesinfo") //reference to sub node

        reference.push().setValue(note).addOnSuccessListener {
            Toast.makeText(applicationContext,"Note Saved",Toast.LENGTH_SHORT).show()
        }.addOnCanceledListener {
            Toast.makeText(applicationContext,"Unable to save your note",Toast.LENGTH_SHORT).show()
        }



    }

}