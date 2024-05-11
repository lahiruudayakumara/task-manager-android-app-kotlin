package com.example.taskmanagerapp.model

data class TaskModel(

    val id: Int,
    val title: String,
    val description: String,
    val selectDate: String,
    val selectTime: String,
    val priority: String,
    val status: String
)
