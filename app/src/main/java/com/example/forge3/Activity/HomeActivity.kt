package com.example.forge3.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.forge3.Adapter.NoteAdapter
import com.example.forge3.DataModel.FirebaseData
import com.example.forge3.Model.NotesModel
import com.example.forge3.R
import com.example.forge3.ViewModel.HomeViewModel
import com.example.forge3.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private var noteList:MutableList<NotesModel> = mutableListOf()
    private lateinit var binding :ActivityHomeBinding
    private var viewModel :HomeViewModel ? = null
    private var noteListLiveData = MutableLiveData<List<NotesModel>>()
   var  customAdapter :NoteAdapter? = null





    init {
        customAdapter = NoteAdapter(noteList)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = customAdapter
        }


        //FAB button
        //opening new MakeNoteActivity if user clicks on FAB
        binding.fab.setOnClickListener {
            val intent = Intent(this, MakeNoteActivity::class.java)
            startActivity(intent)
        }

        //swipe refresh layout
        //currently working
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false

            noteListLiveData = viewModel!!.getNotesList(applicationContext)

            binding.recyclerView.adapter?.notifyDataSetChanged()

            viewModel!!.getNotesList(applicationContext).observe(this) { noteList ->
                customAdapter?.updateNotes(noteList)
                Log.e("tag", "$noteList")
            }
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
        }
    }


}