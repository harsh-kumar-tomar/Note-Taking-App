package com.example.forge3

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager

class MakeNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_note)

        // Set the navigation bar to be transparent
//        window.setDecorFitsSystemWindows(false)
        window.navigationBarColor = Color.TRANSPARENT
        // Hide the navigation bar



    }
}