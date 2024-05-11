package com.example.taskmanagerapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanagerapp.db.DBOpenHelper
import com.example.taskmanagerapp.utils.COLUMN_NAME_DATE
import com.example.taskmanagerapp.utils.COLUMN_NAME_DESCRIPTION
import com.example.taskmanagerapp.utils.COLUMN_NAME_TIME
import com.example.taskmanagerapp.utils.COLUMN_NAME_TITLE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar

class UpdateTaskActivity : AppCompatActivity() {


    private lateinit var etUpdatedTitle: TextInputLayout
    private lateinit var etUpdatedDescription: TextInputLayout
    private lateinit var fabUpdate: FloatingActionButton
    private lateinit var calendar: Calendar
    private val dbOpenHelper = DBOpenHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        etUpdatedTitle = findViewById(R.id.edit_title)
        etUpdatedDescription = findViewById(R.id.edit_description)
        fabUpdate = findViewById(R.id.fab_update)

        val titleOld = intent.getStringExtra(COLUMN_NAME_TITLE)
        val descriptionOld = intent.getStringExtra(COLUMN_NAME_DESCRIPTION)
        val datepre = intent.getStringExtra(COLUMN_NAME_DATE)
        val timepre = intent.getStringExtra(COLUMN_NAME_TIME)

        calendar = Calendar.getInstance()
        datepre?.let {
            val dateParts = it.split("-")
            val year = dateParts[0].toInt()
            val month = dateParts[1].toInt() - 1 // Month starts from 0
            val day = dateParts[2].toInt()
            calendar.set(year, month, day)
        }

        timepre?.let {
            val timeParts = it.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
        }

        val btnPickDate = findViewById<Button>(R.id.btn_pick_date)
        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        val btnPickTime = findViewById<Button>(R.id.btn_pick_time)
        btnPickTime.setOnClickListener {
            showTimePicker()
        }


        if (!titleOld.isNullOrBlank()) {

            etUpdatedTitle.editText?.text =
                Editable.Factory.getInstance().newEditable(titleOld)
            etUpdatedDescription.editText?.text =
                Editable.Factory.getInstance().newEditable(descriptionOld)

            Log.d("UpdateNoteActivity", titleOld.toString())
            Log.d("UpdateNoteActivity", descriptionOld.toString())
            Log.d("UpdateNoteActivity", datepre.toString())
            Log.d("UpdateNoteActivity", timepre.toString())
        } else {
            Log.d("UpdateNoteActivity", "value was null")
            Toast.makeText(this, "Value was null", Toast.LENGTH_SHORT).show()
        }


        fabUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
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
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun updateData() {

        val id = intent.getIntExtra(BaseColumns._ID, 0).toString()

        if (etUpdatedTitle.editText?.text.toString().isEmpty()) {
            etUpdatedTitle.error = "Please enter your Title"
            etUpdatedTitle.requestFocus()
            return
        }

        if (etUpdatedDescription.editText?.text.toString().isEmpty()) {
            etUpdatedDescription.error = "Please enter your Description"
            etUpdatedDescription.requestFocus()
            return
        }
        val selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        val selectedTime = SimpleDateFormat("HH:mm").format(calendar.time)
        if (notEmpty()) {

            dbOpenHelper.updateTask(
                id,
                etUpdatedTitle.editText?.text.toString(),
                etUpdatedDescription.editText?.text.toString(),
                selectedDate,
                selectedTime
            )
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }

    }

    private fun notEmpty(): Boolean {
        return (etUpdatedTitle.editText?.text.toString().isNotEmpty()
                && etUpdatedDescription.editText?.text.toString().isNotEmpty())
    }

}