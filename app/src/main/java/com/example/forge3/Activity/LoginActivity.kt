package com.example.forge3.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.forge3.Model.UserSettingModel
import com.example.forge3.R
import com.example.forge3.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    val RC_SIGN_IN = 1200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )

        //adding google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        //checking if the user is already logged in or not
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null)
        {
            val intent = Intent(applicationContext , HomeActivity::class.java)
            startActivity(intent)
        }

        //GoogleSignIn btn onclick listener
        binding.GoogleSignIn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

    }

    // Result returned from launching the Intent from GoogleSignInClient.SignInIntent(...);
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Toast.makeText(applicationContext , "Success",Toast.LENGTH_SHORT).show()

                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

                //adding user ID and user Name to firebase realtime dataBase
                val firebaseDatabase = FirebaseDatabase.getInstance()
                val reference = firebaseDatabase.getReference("")
                reference.child("Users").child(account.id.toString()).setValue(account.displayName)

                //using save preferences
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("userId", account.id).apply()


                //saving data to firestore
                val db = FirebaseFirestore.getInstance()

                val userNotesRef = db.collection("Users").document(account.id!!)

                val userSettingModel = UserSettingModel(account.id.toString() , account.displayName.toString() , account.email.toString() , account.photoUrl.toString())

                userNotesRef.set(userSettingModel).addOnSuccessListener {
                    Toast.makeText(applicationContext, "Details Saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Unable to  save your details", Toast.LENGTH_SHORT).show()
                }


                //starting new activity
                val intent = Intent(applicationContext , HomeActivity::class.java)
                startActivity(intent)
                finish()


            } catch (e: ApiException) {

                //if any exception occurs
                Toast.makeText(applicationContext , "Fail$e",Toast.LENGTH_SHORT).show()
                Log.e("Error", "onActivityResult: $e")

            }
        }else {
            Toast.makeText(applicationContext , "Error while sign in",Toast.LENGTH_SHORT).show()

        }
    }
}