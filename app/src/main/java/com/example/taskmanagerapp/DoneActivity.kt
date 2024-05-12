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

class DoneActivity : AppCompatActivity() {

    private lateinit var doneActBcack: ImageButton
    private lateinit var doneRecyclerView: RecyclerView

    private val dbOpenHelper by lazy { DBOpenHelper(this) }
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_done)

        doneActBcack = findViewById(R.id.fab_done_back_main)
        doneRecyclerView = findViewById(R.id.done_recycler_view)

        setupRecyclerView(doneRecyclerView)

        job = CoroutineScope(Dispatchers.Main).launch {
            doneRecyclerView.adapter = TaskAdapter(this@DoneActivity, fetchTasksFromDatabase("Done"))
        }

        doneActBcack.setOnClickListener {
            doneBackToMain()
        }

    }

    private fun doneBackToMain() {
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the coroutine when the activity is destroyed
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DoneActivity, LinearLayoutManager.VERTICAL, false)
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