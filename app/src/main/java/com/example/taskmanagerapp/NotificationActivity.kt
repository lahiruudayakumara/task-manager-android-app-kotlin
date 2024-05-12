package com.example.taskmanagerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import java.time.LocalDate

class NotificationActivity : AppCompatActivity() {

    private lateinit var notifyBcack: ImageButton
    private lateinit var viewDate: TextView
    private lateinit var notifyRecyclerView: RecyclerView

    private val dbOpenHelper by lazy { DBOpenHelper(this) }
    private lateinit var job: Job

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)

        val currentDate = LocalDate.now()

        notifyBcack = findViewById(R.id.notify_back_main)
        viewDate = findViewById(R.id.displayDate)
        notifyRecyclerView = findViewById(R.id.notify_recycler_view)

        viewDate.text = "$currentDate"

        setupRecyclerView(notifyRecyclerView)

        job = CoroutineScope(Dispatchers.Main).launch {
            notifyRecyclerView.adapter = TaskAdapter(this@NotificationActivity, fetchTasksFromDatabase("Pending", "$currentDate"))
        }

        notifyBcack.setOnClickListener {
            notifyBackToMain()
        }
    }

    private fun notifyBackToMain() {
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the coroutine when the activity is destroyed
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            //Hide Scroll Bar
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
        }
    }


    private suspend fun fetchTasksFromDatabase(status: String, date: String): MutableList<TaskModel> {
        return withContext(Dispatchers.IO) {
            dbOpenHelper.readTaskByDate(status, date)
        }
    }
}