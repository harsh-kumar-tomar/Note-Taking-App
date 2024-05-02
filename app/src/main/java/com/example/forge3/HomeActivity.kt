package com.example.forge3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.forge3.databinding.ActivityHomeBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    var noteList:MutableList<NotesModel> = mutableListOf()
    lateinit var binding :ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)


        Thread{
            fetchNoteFromFirebase()
        }.start()

        //building recyclerView
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity,1)
            adapter = NoteAdapter(noteList)
        }


        //opening new MakeNoteActivity if user clicks on FAB
        binding.fab.setOnClickListener {
            val intent = Intent(this,MakeNoteActivity::class.java)
            startActivity(intent)
        }

        //currently working
        binding.swipeRefreshLayout.setOnRefreshListener {
//            fetchNoteFromFirebase()
            binding.swipeRefreshLayout.isRefreshing = false

        }

    }

    private fun fetchNoteFromFirebase() {

        binding.swipeRefreshLayout.isRefreshing = false
        //retrieving userID fro sharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        // reference to root node
        val rootNode = FirebaseDatabase.getInstance()
        val reference = rootNode.getReference("").child("Users").child(userId!!).child("notesinfo")


        //filling notes data in noteList
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val note = snapshot.getValue(NotesModel::class.java)
                note?.let {
                    noteList.add(it)
                    binding.recyclerView.adapter?.notifyItemInserted(noteList.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Failed to retrieve notes: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}