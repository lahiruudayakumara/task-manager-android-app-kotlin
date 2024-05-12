package com.example.taskmanagerapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.adapter.TaskAdapter
import com.example.taskmanagerapp.db.DBOpenHelper
import com.example.taskmanagerapp.model.TaskModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBcack: ImageButton
    private lateinit var searchRecyclerView: RecyclerView

    private val dbOpenHelper by lazy { DBOpenHelper(this) }
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        searchBcack = findViewById(R.id.fab_search_back_main)
        searchRecyclerView = findViewById(R.id.search_recycler_view)

        setupRecyclerView(searchRecyclerView)

        job = CoroutineScope(Dispatchers.Main).launch {
            searchRecyclerView.adapter = TaskAdapter(this@SearchActivity, fetchTasksFromDatabase("In Progress"))
        }

        searchBcack.setOnClickListener {
            searchBackToMain()
        }

    }

    private fun searchBackToMain() {
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the coroutine when the activity is destroyed
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            //Hide Scroll Bar
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
        }
    }


    private suspend fun fetchTasksFromDatabase(status: String): MutableList<TaskModel> {
        return withContext(Dispatchers.IO) {
            dbOpenHelper.readTaskByStatus(status)
        }
    }
}