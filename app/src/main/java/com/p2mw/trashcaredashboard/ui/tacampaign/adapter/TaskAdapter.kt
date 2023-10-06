package com.p2mw.trashcaredashboard.ui.tacampaign.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.p2mw.trashcaredashboard.databinding.ItemTaskBinding
import com.p2mw.trashcaredashboard.model.task.Task
import com.p2mw.trashcaredashboard.ui.tacampaign.TaCampaignDetailActivity
import com.p2mw.trashcaredashboard.utils.Constants.TASK
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    inner class TaskViewHolder(private val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Task) {
            binding.apply {
                tvIdUser.text = "User ID: ${item.userId}"
                tvIdTugas.text = item.id
                tvStatusTugas.text = item.taskStatus
                tvWaktuSubmit.text = item.dateSubmitted?.let {
                    SimpleDateFormat(
                        "yyyy-MM-dd | hh:mm",
                        Locale.ENGLISH
                    ).format(it)
                }
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id + oldItem.userId == newItem.id + newItem.userId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), null, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            Intent(context, TaCampaignDetailActivity::class.java).also {
                it.putExtra(TASK, item)
                context.startActivity(it)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}