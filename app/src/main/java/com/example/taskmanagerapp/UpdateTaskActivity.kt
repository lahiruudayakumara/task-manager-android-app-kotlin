package com.example.taskmanagerapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanagerapp.db.DBOpenHelper
import com.example.taskmanagerapp.utils.COLUMN_NAME_DATE
import com.example.taskmanagerapp.utils.COLUMN_NAME_DESCRIPTION
import com.example.taskmanagerapp.utils.COLUMN_NAME_STATUS
import com.example.taskmanagerapp.utils.COLUMN_NAME_TIME
import com.example.taskmanagerapp.utils.COLUMN_NAME_TITLE
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar

class UpdateTaskActivity : AppCompatActivity() {


    private lateinit var etUpdatedTitle: TextInputLayout
    private lateinit var etUpdatedDescription: TextInputLayout
    private lateinit var fabUpdate: ImageButton
    private lateinit var fabDelete: ImageButton
    private lateinit var backToMain: ImageButton
    private lateinit var calendar: Calendar
    private lateinit var etDate: TextView
    private lateinit var etTime: TextView

    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var status: String = ""

    private lateinit var checkBoxPending: CheckBox
    private lateinit var checkBoxInProgress: CheckBox
    private lateinit var checkBoxDone: CheckBox

    private val dbOpenHelper = DBOpenHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        etUpdatedTitle = findViewById(R.id.edit_title)
        etUpdatedDescription = findViewById(R.id.edit_description)
        fabUpdate = findViewById(R.id.fab_update)
        backToMain = findViewById(R.id.fab_back_main)
        fabDelete = findViewById(R.id.fab_delete)

        checkBoxPending = findViewById(R.id.edit_pending)
        checkBoxInProgress = findViewById(R.id.edit_in_progress)
        checkBoxDone = findViewById(R.id.edit_done)

        etDate = findViewById(R.id.display_edit_date)
        etTime = findViewById(R.id.display_edit_time)

        calendar = Calendar.getInstance()

        val titleOld = intent.getStringExtra(COLUMN_NAME_TITLE)
        val descriptionOld = intent.getStringExtra(COLUMN_NAME_DESCRIPTION)
        val datepre = intent.getStringExtra(COLUMN_NAME_DATE)
        val timepre = intent.getStringExtra(COLUMN_NAME_TIME)
        val statusOld = intent.getStringExtra(COLUMN_NAME_STATUS)

        etDate.text = datepre
        etTime.text = timepre

        setStatusCheckBoxes(statusOld)

        checkBoxPending.setOnCheckedChangeListener { _, isChecked -> setStatus(isChecked, "Pending") }
        checkBoxInProgress.setOnCheckedChangeListener { _, isChecked -> setStatus(isChecked, "In Progress") }
        checkBoxDone.setOnCheckedChangeListener { _, isChecked -> setStatus(isChecked, "Done") }

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

        val btnPickDate = findViewById<Button>(R.id.btn_edit_pick_date)
        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        val btnPickTime = findViewById<Button>(R.id.btn_edit_pick_time)
        btnPickTime.setOnClickListener {
            showTimePicker()
        }


        if (!titleOld.isNullOrBlank()) {

            etUpdatedTitle.editText?.text =
                Editable.Factory.getInstance().newEditable(titleOld)
            etUpdatedDescription.editText?.text =
                Editable.Factory.getInstance().newEditable(descriptionOld)

            Log.d("UpdateTaskActivity", titleOld.toString())
            Log.d("UpdateTaskActivity", descriptionOld.toString())
            Log.d("UpdateTaskActivity", datepre.toString())
            Log.d("UpdateTaskActivity", timepre.toString())
        } else {
            Log.d("UpdateTaskActivity", "value was null")
            Toast.makeText(this, "Value was null", Toast.LENGTH_SHORT).show()
        }


        fabUpdate.setOnClickListener {
            updateData()
        }

        backToMain.setOnClickListener {
            fabBackToMain()
        }

        fabDelete.setOnClickListener {
            deleteTask()
        }
    }

    private fun setStatusCheckBoxes(statusOld: String?) {
        when (statusOld) {
            "Pending" -> checkBoxPending.isChecked = true
            "In Progress" -> checkBoxInProgress.isChecked = true
            "Done" -> checkBoxDone.isChecked = true
        }
    }

    private fun setStatus(isChecked: Boolean, newStatus: String) {
        if (isChecked) {
            status = newStatus
            when (newStatus) {
                "Pending" -> {
                    checkBoxInProgress.isChecked = false
                    checkBoxDone.isChecked = false
                }
                "In Progress" -> {
                    checkBoxPending.isChecked = false
                    checkBoxDone.isChecked = false
                }
                "Done" -> {
                    checkBoxPending.isChecked = false
                    checkBoxInProgress.isChecked = false
                }
            }
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
        selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        selectedTime = SimpleDateFormat("HH:mm").format(calendar.time)
        if (notEmpty()) {

            dbOpenHelper.updateTask(
                id,
                etUpdatedTitle.editText?.text.toString(),
                etUpdatedDescription.editText?.text.toString(),
                selectedDate,
                selectedTime,
                status
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

    private fun deleteTask() {
        val id = intent.getIntExtra(BaseColumns._ID, 0).toString()
        dbOpenHelper.deleteTask(id)
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
        finish()
    }

    private fun fabBackToMain() {
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }

    private fun updateDateTextView() {
        etDate = findViewById(R.id.display_edit_date)
        etDate.text = "$selectedDate"
    }

    private fun updateTimeTextView() {
        etTime = findViewById(R.id.display_edit_time)
        etTime.text = "$selectedTime"
    }

}