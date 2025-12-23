package com.android.seclearning.ui.main.home.personal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.R
import com.android.seclearning.view.MyTextView

class TaskAdapter(
    private val goals: List<String>,
    private val startIndex: Int,
    private val completed: Int,
    private val onTaskChecked: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskVH(view)
    }

    override fun getItemCount() = goals.size

    override fun onBindViewHolder(holder: TaskVH, position: Int) {
        val globalIndex = startIndex + position
        holder.bind(goals[position], globalIndex)
    }

    inner class TaskVH(view: View) : RecyclerView.ViewHolder(view) {

        private val ivLock = view.findViewById<AppCompatImageView>(R.id.ic_lock)
        private val cb = view.findViewById<AppCompatCheckBox>(R.id.checkbox)
        private val tvTask = view.findViewById<MyTextView>(R.id.tv_task)

        fun bind(text: String, index: Int) {
            tvTask.text = text

            cb.setOnCheckedChangeListener(null)

            when {
                index < completed -> {
                    ivLock.visibility = View.GONE
                    cb.visibility = View.VISIBLE
                    cb.isChecked = true
                    cb.isEnabled = false
                }

                index == completed -> {
                    ivLock.visibility = View.GONE
                    cb.visibility = View.VISIBLE
                    cb.isChecked = false
                    cb.isEnabled = true
                }

                else -> {
                    ivLock.visibility = View.VISIBLE
                    cb.visibility = View.GONE
                }
            }

            cb.setOnCheckedChangeListener { buttonView, isChecked ->
                // Chỉ xử lý nếu đây là hành động bấm từ người dùng
                if (buttonView.isPressed && isChecked && index == completed) {
                    onTaskChecked(index) // Truyền index hiện tại lên, API sẽ tự +1
                }
            }
        }
    }
}

