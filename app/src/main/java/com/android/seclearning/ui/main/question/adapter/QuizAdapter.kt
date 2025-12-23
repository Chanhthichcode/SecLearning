package com.android.seclearning.ui.main.question.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.data.model.QuizModel
import com.android.seclearning.databinding.ItemQuizQuestionBinding

class QuizAdapter() : RecyclerView.Adapter<QuizViewHolder>() {

    private val listData = mutableListOf<QuizModel>()

    private var onOptionSelected: ((Int, Int) -> Unit)? = null
    fun onOptionSelected(onOptionSelected: (Int, Int) -> Unit) {
        this.onOptionSelected = onOptionSelected
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<QuizModel>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding =
            ItemQuizQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val item = listData[position]
        holder.bind(item, position) { qId, optIndex ->
            onOptionSelected?.invoke(qId, optIndex)
        }
    }


    override fun getItemCount(): Int = listData.size
}