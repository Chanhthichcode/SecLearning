package com.android.seclearning.ui.main.question.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.databinding.ItemQuizQuestionBinding

class QuestionAdapter() : RecyclerView.Adapter<QuestionViewHolder>() {

    private var listData = listOf<QuestionModel>()

    private var onOptionSelected: ((Int, Int) -> Unit)? = null
    fun onOptionSelected(onOptionSelected: (Int, Int) -> Unit) {
        this.onOptionSelected = onOptionSelected
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<QuestionModel>) {
        listData = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding =
            ItemQuizQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        listData.getOrNull(position)?.let { mItem ->
            holder.bind(mItem) { optionIndex ->
                onOptionSelected?.invoke(mItem.questionId, optionIndex)
            }
        }
    }

    override fun getItemCount(): Int = listData.size
}