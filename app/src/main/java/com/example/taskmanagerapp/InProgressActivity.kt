package com.example.taskmanagerapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

class InProgressActivity : AppCompatActivity() {

    private lateinit var inProgressBcack: ImageButton
    private lateinit var inProgressRecyclerView: RecyclerView

    private val dbOpenHelper by lazy { DBOpenHelper(this) }
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_in_progress)

        inProgressBcack = findViewById(R.id.fab_progress_back_main)
        inProgressRecyclerView = findViewById(R.id.in_progress_recycler_view)

        setupRecyclerView(inProgressRecyclerView)

        job = CoroutineScope(Dispatchers.Main).launch {
            inProgressRecyclerView.adapter = TaskAdapter(this@InProgressActivity, fetchTasksFromDatabase("In Progress"))
        }

        inProgressBcack.setOnClickListener {
            inProgressBackToMain()
        }
    }

    private fun inProgressBackToMain() {
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the coroutine when the activity is destroyed
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@InProgressActivity, LinearLayoutManager.VERTICAL, false)
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