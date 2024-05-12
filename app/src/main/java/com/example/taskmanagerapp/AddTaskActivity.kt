package com.example.taskmanagerapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanagerapp.db.DBOpenHelper
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTitle: TextInputLayout
    private lateinit var etDescription: TextInputLayout
    private lateinit var fabSend: ImageButton
    private lateinit var fabBcack: ImageButton
    private lateinit var calendar: Calendar

    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView

    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var priority: String = "Important"
    private var status: String = "Pending"

    private lateinit var checkBoxImportant: CheckBox
    private lateinit var checkBoxToDo: CheckBox
    private lateinit var checkBoxMeeting: CheckBox

    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button

    private val dbOpenHelper = DBOpenHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        fabSend = findViewById(R.id.fab_send)

        calendar = Calendar.getInstance()

        selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        selectedTime = SimpleDateFormat("HH:mm").format(calendar.time)

        checkBoxImportant = findViewById<CheckBox>(R.id.important)
        checkBoxToDo = findViewById<CheckBox>(R.id.to_do)
        checkBoxMeeting = findViewById<CheckBox>(R.id.meeting)

        checkBoxImportant.isChecked = true;

        checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                priority = "Important"
                checkBoxToDo.isChecked = false
                checkBoxMeeting.isChecked = false
            }
        }

        checkBoxToDo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                priority = "To Do"
                checkBoxImportant.isChecked = false
                checkBoxMeeting.isChecked = false
            }
        }

        checkBoxMeeting.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                priority = "Meeting"
                checkBoxImportant.isChecked = false
                checkBoxToDo.isChecked = false
            }
        }

        updateDateTextView()
        updateTimeTextView()

        btnPickDate = findViewById<Button>(R.id.btn_pick_date)
        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        btnPickTime = findViewById<Button>(R.id.btn_pick_time)
        btnPickTime.setOnClickListener {
            showTimePicker()
        }

        fabSend.setOnClickListener {
            fabSendData()
        }

        fabBcack = findViewById<ImageButton>(R.id.fab_back)
        fabBcack.setOnClickListener {
            fabBackToMain()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
                updateDateTextView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                selectedTime = SimpleDateFormat("HH:mm").format(calendar.time)
                updateTimeTextView()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()

    }

    private fun fabBackToMain() {
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }

    private fun fabSendData() {
        if (etTitle.editText?.text.toString().isEmpty()) {
            etTitle.error = "Please enter your Title"
            etTitle.requestFocus()
            return
        }

        if (etDescription.editText?.text.toString().isEmpty()) {
            etDescription.error = "Please enter your Description"
            etDescription.requestFocus()
            return
        }

        selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        selectedTime = SimpleDateFormat("HH:mm").format(calendar.time)

         dbOpenHelper.addTask(
            etTitle.editText?.text.toString(),
            etDescription.editText?.text.toString(),
            selectedDate,
            selectedTime,
             priority,
             status
        )
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
        finish()
    }

    private fun updateDateTextView() {
        dateTextView = findViewById<TextView>(R.id.display_data)
        dateTextView.text = "$selectedDate"
    }

    private fun updateTimeTextView() {
        timeTextView = findViewById<TextView>(R.id.display_time)
        timeTextView.text = "$selectedTime"
    }

}