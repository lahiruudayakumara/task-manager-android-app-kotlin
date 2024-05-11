package com.example.taskmanagerapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.adapter.TaskAdapter
import com.example.taskmanagerapp.db.DBOpenHelper
import com.example.taskmanagerapp.model.TaskModel

class MainActivity : AppCompatActivity() {

    private lateinit var importantRecyclerView: RecyclerView
    private lateinit var pendingRecyclerView: RecyclerView
    private lateinit var meetingRecyclerView: RecyclerView

    private lateinit var fabCreate: ImageButton

    private lateinit var importantDataset: MutableList<TaskModel>
    private lateinit var pendingDataset: MutableList<TaskModel>
    private lateinit var meetingDataset: MutableList<TaskModel>

    private val dbOpenHelper = DBOpenHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        importantRecyclerView = findViewById(R.id.important_recycler_view)
        pendingRecyclerView = findViewById(R.id.pending_recycler_view)
        meetingRecyclerView = findViewById(R.id.meeting_recycler_view)

        fabCreate = findViewById(R.id.fab_create)

        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        importantRecyclerView.layoutManager = layoutManager1

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        pendingRecyclerView.layoutManager = layoutManager2

        val layoutManager3 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        meetingRecyclerView.layoutManager = layoutManager3

        importantDataset = dbOpenHelper.readTask("Pending", "Important")
        pendingDataset = dbOpenHelper.readTask("Pending", "To Do")
        meetingDataset = dbOpenHelper.readTask("Pending", "Meeting")

        importantRecyclerView.adapter = TaskAdapter(this, importantDataset)
        importantRecyclerView.setHasFixedSize(true)

        pendingRecyclerView.adapter = TaskAdapter(this, pendingDataset)
        pendingRecyclerView.setHasFixedSize(true)

        meetingRecyclerView.adapter = TaskAdapter(this, meetingDataset)
        meetingRecyclerView.setHasFixedSize(true)


        fabCreate.setOnClickListener {
            val intentToAddNoteActivity = Intent(this, AddTaskActivity::class.java)
            startActivity(intentToAddNoteActivity)
            finish()
        }

    }
}