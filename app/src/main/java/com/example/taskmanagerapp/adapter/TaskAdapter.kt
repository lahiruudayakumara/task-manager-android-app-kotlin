package com.example.taskmanagerapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.UpdateTaskActivity
import com.example.taskmanagerapp.model.TaskModel
import com.example.taskmanagerapp.utils.COLUMN_NAME_DATE
import com.example.taskmanagerapp.utils.COLUMN_NAME_DESCRIPTION
import com.example.taskmanagerapp.utils.COLUMN_NAME_STATUS
import com.example.taskmanagerapp.utils.COLUMN_NAME_TIME
import com.example.taskmanagerapp.utils.COLUMN_NAME_TITLE
import com.example.taskmanagerapp.utils.DialogBox

class TaskAdapter(

    private val context: Context,
    private val dataSet: List<TaskModel>

) : RecyclerView.Adapter<TaskAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textDescription: TextView = view.findViewById(R.id.text_description)
        val textDate: TextView = view.findViewById(R.id.viewDate)
        val textTime: TextView = view.findViewById(R.id.viewTime)
        val textStatus: TextView = view.findViewById(R.id.viewStatus)
        val cardView = view.findViewById<CardView>(R.id.mainCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_recycler_single_item, parent, false)

        return NoteViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

//        val dialog = DialogBox()
        val item = dataSet[position]

        holder.textTitle.text = item.title
        holder.textDescription.text = item.description
        holder.textDate.text = item.selectDate
        holder.textTime.text = item.selectTime
        holder.textStatus.text = item.status

        // Set OnClickListener to the CardView
        holder.cardView.setOnClickListener {
            val intent = Intent(context, UpdateTaskActivity::class.java).apply {
                putExtra(BaseColumns._ID, item.id)
                putExtra(COLUMN_NAME_TITLE, item.title)
                putExtra(COLUMN_NAME_DESCRIPTION, item.description)
                putExtra(COLUMN_NAME_DATE, item.selectDate)
                putExtra(COLUMN_NAME_TIME, item.selectTime)
                putExtra(COLUMN_NAME_STATUS, item.status)
            }
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}