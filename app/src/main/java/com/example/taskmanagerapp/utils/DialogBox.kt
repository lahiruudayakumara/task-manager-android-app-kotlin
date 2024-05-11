package com.example.taskmanagerapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import com.example.taskmanagerapp.MainActivity
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.UpdateTaskActivity
import com.example.taskmanagerapp.db.DBOpenHelper
import com.example.taskmanagerapp.model.TaskModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DialogBox {

    private val TAG = "DialogBox"

    fun editDialog(context: Context, note: TaskModel) {

        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle("Edit")
        dialog.setMessage("Do you want to update?")
        dialog.setIcon(R.drawable.ic_baseline_edit_24)

        Log.d(TAG, note.id.toString())
        Log.d(TAG, note.title)
        Log.d(TAG, note.description)
        Log.d(TAG,note.selectDate)
        Log.d(TAG,note.selectTime)

        dialog.setPositiveButton("Update") { _, _ ->

            val intent = Intent(context, UpdateTaskActivity::class.java).apply {
                putExtra(BaseColumns._ID, note.id)
                putExtra(COLUMN_NAME_TITLE, note.title)
                putExtra(COLUMN_NAME_DESCRIPTION, note.description)
                putExtra(COLUMN_NAME_DATE,note.selectDate)
                putExtra(COLUMN_NAME_TIME,note.selectTime)
            }
            context.startActivity(intent)
            (context as Activity).finish()

        }

        dialog.setNeutralButton("Cancel") { _, _ ->
            Toast.makeText(context, "Cancelled!", Toast.LENGTH_SHORT).show()
        }

        dialog.create()
        dialog.setCancelable(false)
        dialog.show()
    }

    fun deleteDialog(context: Context, note: TaskModel) {

        val dbOpenHelper = DBOpenHelper(context)

        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle("Delete")
        dialog.setMessage("Do you really want to delete?")
        dialog.setIcon(R.drawable.ic_baseline_delete_forever_24)

        dialog.setPositiveButton("Delete") { _, _ ->

            Log.d(TAG, note.id.toString())
            dbOpenHelper.deleteTask(note.id.toString())

            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()

        }

        dialog.setNeutralButton("Cancel") { _, _ ->
            Toast.makeText(context, "Cancelled!", Toast.LENGTH_SHORT).show()
        }

        dialog.create()
        dialog.setCancelable(false)
        dialog.show()
    }

}