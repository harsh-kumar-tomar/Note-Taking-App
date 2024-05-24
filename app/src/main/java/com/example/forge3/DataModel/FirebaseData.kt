package com.example.forge3.DataModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.forge3.Model.NotesModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class FirebaseData(val context: Context){

    private var tempList:MutableList<NotesModel> = mutableListOf()
    private var noteList:MutableList<NotesModel> = mutableListOf()

    private val noteListLiveData = MutableLiveData<List<NotesModel>>()

    init {
        fetchList()
    }



    private fun fetchList(): MutableList<NotesModel> {

        val userId = getUserID()

        // reference to root node
        val rootNode = FirebaseDatabase.getInstance()
        val reference = rootNode.getReference("").child("Users").child(userId!!).child("notesinfo")


        //filling notes data in noteList
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?)
            {
                val note = snapshot.getValue(NotesModel::class.java)
//                note?.let {
//                    tempList.add(it)
//                    Log.e("tag","inside firebasedata + $tempList")
//                }
                note?.let {
                    val currentList = noteListLiveData.value ?: emptyList()
                    val updatedList = currentList.toMutableList()
                    updatedList.add(it)
                    noteListLiveData.value = updatedList
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Failed to retrieve notes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        noteList.addAll(tempList)

        return noteList
    }

    fun getList(): MutableLiveData<List<NotesModel>> {
        return noteListLiveData
    }

    private fun getUserID(): String? {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

//        val user = FirebaseAuth.getInstance().currentUser
//        return user!!.uid

        return sharedPreferences.getString("userId", null)
    }
}