package com.example.taskmanagerapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.taskmanagerapp.model.TaskModel
import com.example.taskmanagerapp.utils.COLUMN_NAME_DATE
import com.example.taskmanagerapp.utils.COLUMN_NAME_DESCRIPTION
import com.example.taskmanagerapp.utils.COLUMN_NAME_TIME
import com.example.taskmanagerapp.utils.COLUMN_NAME_TITLE
import com.example.taskmanagerapp.utils.COLUMN_NAME_PRIORITY
import com.example.taskmanagerapp.utils.COLUMN_NAME_STATUS
import com.example.taskmanagerapp.utils.TABLE_NAME


private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_TITLE TEXT," +
            "$COLUMN_NAME_DESCRIPTION TEXT,"+
            "$COLUMN_NAME_DATE TEXT,"+
            "$COLUMN_NAME_TIME TEXT,"+
            "$COLUMN_NAME_PRIORITY TEXT,"+
            "$COLUMN_NAME_STATUS TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"


class DBOpenHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        // If you change the database schema, you must increment the database version.
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskManager.dp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addTask(title: String, description: String, selectedDate: String, selectedTime: String, priority: String, status: String) {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_DESCRIPTION, description)
            put(COLUMN_NAME_DATE,selectedDate)
            put(COLUMN_NAME_TIME,selectedTime)
            put(COLUMN_NAME_PRIORITY, priority)
            put(COLUMN_NAME_STATUS, status)
        }
        db?.insert(TABLE_NAME, null, values)
        db.close()

    }

    fun readTask(status: String, priority: String): MutableList<TaskModel> {
        val db = this.readableDatabase
        val cursorTask: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE TaskStatus = ? AND TaskPriority = ?", arrayOf(status, priority))
        val notesList: MutableList<TaskModel> = mutableListOf()

        if (cursorTask.moveToFirst()) {
            do {
                Log.d("DPOpenHelper", cursorTask.getString(0))
                notesList.add(
                    TaskModel(
                        cursorTask.getInt(0),
                        cursorTask.getString(1),
                        cursorTask.getString(2),
                        cursorTask.getString(3),
                        cursorTask.getString(4),
                        cursorTask.getString(5),
                        cursorTask.getString(6),
                    )
                )
            } while (cursorTask.moveToNext())
        }
        cursorTask.close()
        return notesList
    }

    fun readTaskCount(status: String, priority: String): Int {
        val db = this.readableDatabase
        val cursorTask: Cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME WHERE TaskStatus = ? AND TaskPriority = ?", arrayOf(status, priority))
        var count = 0

        if (cursorTask.moveToFirst()) {
            count = cursorTask.getInt(0)
        }
        cursorTask.close()
        return count
    }

    fun readTaskByStatus(status: String): MutableList<TaskModel> {
        val db = this.readableDatabase
        val cursorTask: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE TaskStatus = ?", arrayOf(status))
        val notesList: MutableList<TaskModel> = mutableListOf()

        if (cursorTask.moveToFirst()) {
            do {
                Log.d("DPOpenHelper", cursorTask.getString(0))
                notesList.add(
                    TaskModel(
                        cursorTask.getInt(0),
                        cursorTask.getString(1),
                        cursorTask.getString(2),
                        cursorTask.getString(3),
                        cursorTask.getString(4),
                        cursorTask.getString(5),
                        cursorTask.getString(6),
                    )
                )
            } while (cursorTask.moveToNext())
        }
        cursorTask.close()
        return notesList
    }


    fun readTaskByDate(status: String, selectDate: String): MutableList<TaskModel> {
        val db = this.readableDatabase
        val cursorTask: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE TaskStatus = ? AND TaskDate = ?", arrayOf(status, selectDate))
        val notesList: MutableList<TaskModel> = mutableListOf()

        if (cursorTask.moveToFirst()) {
            do {
                Log.d("DPOpenHelper", cursorTask.getString(0))
                notesList.add(
                    TaskModel(
                        cursorTask.getInt(0),
                        cursorTask.getString(1),
                        cursorTask.getString(2),
                        cursorTask.getString(3),
                        cursorTask.getString(4),
                        cursorTask.getString(5),
                        cursorTask.getString(6)
                    )
                )
            } while (cursorTask.moveToNext())
        }
        cursorTask.close()
        return notesList
    }

    fun readTaskByOnlyDate(selectDate: String): MutableList<TaskModel> {
        val db = this.readableDatabase
        val cursorTask: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE TaskDate = ?", arrayOf(selectDate))
        val notesList: MutableList<TaskModel> = mutableListOf()

        if (cursorTask.moveToFirst()) {
            do {
                Log.d("DPOpenHelper", cursorTask.getString(0))
                notesList.add(
                    TaskModel(
                        cursorTask.getInt(0),
                        cursorTask.getString(1),
                        cursorTask.getString(2),
                        cursorTask.getString(3),
                        cursorTask.getString(4),
                        cursorTask.getString(5),
                        cursorTask.getString(6)
                    )
                )
            } while (cursorTask.moveToNext())
        }
        cursorTask.close()
        return notesList
    }

    fun updateTask(
        id: String,
        title: String,
        description: String,
        selectedDate: String,
        selectedTime: String,
        status: String
    ) {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_DESCRIPTION, description)
            put(COLUMN_NAME_DATE,selectedDate)
            put(COLUMN_NAME_TIME,selectedTime)
            put(COLUMN_NAME_STATUS, status)
        }
        try {
            db?.update(TABLE_NAME, values, "_id = ?", arrayOf(id))
            db.close()
        } catch (e: Exception) {
            Log.d("DBOpenHelper", e.message.toString())
        }
    }

    fun deleteTask(id: String) {

        val db = this.writableDatabase
        try {
            db?.delete(TABLE_NAME, "_id = ?", arrayOf(id))
            db.close()
        } catch (e: Exception) {
            Log.d("DBOpenHelper", e.message.toString())
        }

    }


}