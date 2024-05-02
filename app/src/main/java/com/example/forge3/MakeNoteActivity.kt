package com.example.forge3
import com.example.forge3.databinding.ActivityMakeNoteBinding
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MakeNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMakeNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_make_note)

    }

    //save btn onclick
    fun Save(view: View) {
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