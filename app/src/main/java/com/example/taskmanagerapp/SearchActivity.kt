package com.example.taskmanagerapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
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
import java.text.SimpleDateFormat
import java.util.Calendar

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBcack: ImageButton
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var calendar: Calendar
    private lateinit var btnPickDate:Button

    private lateinit var dateTextView: TextView

    private var selectedDate: String = ""

    private val dbOpenHelper by lazy { DBOpenHelper(this) }
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        searchBcack = findViewById(R.id.fab_search_back_main)
        searchRecyclerView = findViewById(R.id.search_recycler_view)

        calendar = Calendar.getInstance()
        selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        updateDateTextView()

        setupRecyclerView(searchRecyclerView)

        job = CoroutineScope(Dispatchers.Main).launch {
            searchRecyclerView.adapter = TaskAdapter(this@SearchActivity, fetchTasksFromDatabase("$selectedDate"))
        }

        searchBcack.setOnClickListener {
            searchBackToMain()
        }

        btnPickDate = findViewById(R.id.btn_pick_date)
        btnPickDate.setOnClickListener {
            showDatePicker()
        }

    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
                updateDateTextView()
                updateRecyclerView(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
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


    private suspend fun fetchTasksFromDatabase(date: String): MutableList<TaskModel> {
        return withContext(Dispatchers.IO) {
            dbOpenHelper.readTaskByOnlyDate(date)
        }
    }

    private fun updateDateTextView() {
        dateTextView = findViewById(R.id.display_data)
        dateTextView.text = "$selectedDate"
    }

    private fun updateRecyclerView(selectedDate: String) {
        job = CoroutineScope(Dispatchers.Main).launch {
            searchRecyclerView.adapter = TaskAdapter(this@SearchActivity, fetchTasksFromDatabase(selectedDate))
        }
    }


}