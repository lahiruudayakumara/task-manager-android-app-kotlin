package com.example.taskmanagerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.model.TaskModel
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
//        val btnEdit: MaterialButton = view.findViewById(R.id.btn_edit)
//        val btnDelete: MaterialButton = view.findViewById(R.id.btn_delete)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_recycler_single_item, parent, false)

        return NoteViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val dialog = DialogBox()
        val item = dataSet[position]

        holder.textTitle.text = item.title
        holder.textDescription.text = item.description
        holder.textDate.text = item.selectDate
        holder.textTime.text = item.selectTime
        holder.textStatus.text = item.status


//        holder.btnEdit.setOnClickListener {
//            dialog.editDialog(context, item)
//        }
//
//        holder.btnDelete.setOnClickListener {
//            dialog.deleteDialog(context, item)
//        }
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }


}