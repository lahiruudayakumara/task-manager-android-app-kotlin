package com.example.taskmanagerapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

class MainActivity : AppCompatActivity() {

    private lateinit var importantRecyclerView: RecyclerView
    private lateinit var pendingRecyclerView: RecyclerView
    private lateinit var meetingRecyclerView: RecyclerView
    private lateinit var fabCreate: ImageButton
    private lateinit var notificationBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var inProgressBtn: ImageButton
    private lateinit var doneBtn: ImageButton

    private lateinit var importantDisplay: TextView
    private lateinit var toDoDisplay: TextView
    private lateinit var meetingDisplay: TextView

    private val dbOpenHelper by lazy { DBOpenHelper(this) }
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        importantRecyclerView = findViewById(R.id.important_recycler_view)
        pendingRecyclerView = findViewById(R.id.pending_recycler_view)
        meetingRecyclerView = findViewById(R.id.meeting_recycler_view)

        importantDisplay = findViewById(R.id.countImportant)
        toDoDisplay = findViewById(R.id.countToDo)
        meetingDisplay = findViewById(R.id.countMeeting)

        fabCreate = findViewById(R.id.fab_create)
        notificationBtn = findViewById(R.id.notification_bell)
        searchBtn = findViewById(R.id.searchBtn)
        profileBtn = findViewById(R.id.profileBtn)
        doneBtn = findViewById(R.id.doneBtn)
        inProgressBtn = findViewById(R.id.inProgressBtn)

        setupRecyclerView(importantRecyclerView)
        setupRecyclerView(pendingRecyclerView)
        setupRecyclerView(meetingRecyclerView)

        val countImportant = dbOpenHelper.readTaskCount("Pending", "Important")
        importantDisplay.text = countImportant.toString()

        val countToDo = dbOpenHelper.readTaskCount("Pending", "To Do")
        toDoDisplay.text = countToDo.toString()

        val countMeeting = dbOpenHelper.readTaskCount("Pending", "Meeting")
        meetingDisplay.text = countMeeting.toString()

        // Start coroutine to fetch tasks from the database
        job = CoroutineScope(Dispatchers.Main).launch {
            importantRecyclerView.adapter = TaskAdapter(this@MainActivity, fetchTasksFromDatabase("Pending", "Important"))
            pendingRecyclerView.adapter = TaskAdapter(this@MainActivity, fetchTasksFromDatabase("Pending", "To Do"))
            meetingRecyclerView.adapter = TaskAdapter(this@MainActivity, fetchTasksFromDatabase("Pending", "Meeting"))
        }

        fabCreate.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddTaskActivity::class.java))
            finish()
        }

        notificationBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
            finish()
        }

        searchBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            finish()
        }

        inProgressBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, InProgressActivity::class.java))
            finish()
        }

        doneBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, DoneActivity::class.java))
            finish()
        }

        profileBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the coroutine when the activity is destroyed
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            //Hide Scroll Bar
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
        }
    }

    private suspend fun fetchTasksFromDatabase(category: String, type: String): MutableList<TaskModel> {
        return withContext(Dispatchers.IO) {
            dbOpenHelper.readTask(category, type)
        }
    }
}