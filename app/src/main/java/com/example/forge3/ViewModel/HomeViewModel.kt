package com.example.forge3.ViewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forge3.DataModel.FirebaseData
import com.example.forge3.Model.NotesModel


class HomeViewModel() : ViewModel() {
    private lateinit var firebaseData: FirebaseData

    fun getNotesList(context: Context): MutableLiveData<List<NotesModel>> {
            if (!::firebaseData.isInitialized) {
                firebaseData = FirebaseData(context)
            }
            return FirebaseData(context).getList()
    }
}