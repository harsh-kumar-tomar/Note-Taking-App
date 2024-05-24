package com.example.forge3.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.forge3.Model.UserSettingModel
import com.example.forge3.R
import com.example.forge3.databinding.ActivityLoginBinding
import com.example.forge3.databinding.ActivityUserSettingBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class UserSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityUserSettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_setting)

        val userId = getUserID()
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("Users").document(userId!!)

        userDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userSetting = documentSnapshot.toObject(UserSettingModel::class.java)
                binding.userName.text = userSetting!!.userName
                binding.userEmail.text = userSetting.userEmail
                Glide.with(applicationContext)
                    .load(userSetting.userPhoto)
                    .into(binding.userImage)

                Log.d("FirestoreData", "Retrieved user settings: $userSetting")
            } else {
                Toast.makeText(applicationContext, "No such document exists", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, "Failed to retrieve details", Toast.LENGTH_SHORT).show()
            Log.e("FirestoreData", "Error getting document: ", exception)
        }


    }

    private fun getUserID(): String? {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }

}