package com.android.seclearning.ui.main.home.personal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.R
import com.android.seclearning.data.model.TaskLevelModel
import com.android.seclearning.view.MyTextView

class LessonAdapter(
    private var completed: Int,
    private val onTaskCompleted: (Int) -> Unit,
    private val onTaskClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<LessonAdapter.LessonVH>() {

    private val levels = mutableListOf<TaskLevelModel>()

    fun submitList(data: List<TaskLevelModel>) {
        levels.clear()
        levels.addAll(data)
        notifyDataSetChanged()
    }

    fun updateCompleted(value: Int) {
        completed = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonVH(view)
    }

    override fun getItemCount() = levels.size

    override fun onBindViewHolder(holder: LessonVH, position: Int) {
        val level = levels[position]

        // ✅ tính offset đúng mỗi lần bind
        val startIndex = levels
            .take(position)
            .sumOf { it.goals.size }

        holder.bind(level, startIndex, completed)
    }

    inner class LessonVH(view: View) : RecyclerView.ViewHolder(view) {

        private val tvLesson = view.findViewById<MyTextView>(R.id.tv_content)
        private val rvTask = view.findViewById<RecyclerView>(R.id.rcQuiz)

        fun bind(
            level: TaskLevelModel,
            startIndex: Int,
            completed: Int
        ) {
            tvLesson.text = level.name

            rvTask.apply {
                layoutManager = LinearLayoutManager(itemView.context)
                adapter = TaskAdapter(
                    goals = level.goals,
                    startIndex = startIndex,
                    completed = completed,
                    onTaskChecked = {
                        onTaskCompleted(it)
                    },
                    onTaskClick = { index ->
                        onTaskClick?.invoke(index)
                    }
                )
            }
        }
    }
}

