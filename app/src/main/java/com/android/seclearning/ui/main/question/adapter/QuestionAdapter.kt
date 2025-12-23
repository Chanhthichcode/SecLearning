package com.android.seclearning.ui.main.question.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.databinding.ItemQuizQuestionBinding

class QuestionAdapter() : RecyclerView.Adapter<QuestionViewHolder>() {

    private val listData = mutableListOf<QuestionModel>()

    private var onOptionSelected: ((Int, Int) -> Unit)? = null
    fun onOptionSelected(onOptionSelected: (Int, Int) -> Unit) {
        this.onOptionSelected = onOptionSelected
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<QuestionModel>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding =
            ItemQuizQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = listData[position]
        holder.bind(item) { qId, optIndex ->
            onOptionSelected?.invoke(qId, optIndex)
        }
    }


    override fun getItemCount(): Int = listData.size
}