package com.android.seclearning.ui.main.question.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.databinding.ItemQuizQuestionBinding

class QuestionViewHolder(val binding: ItemQuizQuestionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var itemOptionAdapter: ItemOptionAdapter = ItemOptionAdapter()

    init {
        binding.rcQuiz.apply {
            adapter = itemOptionAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(mItem: QuestionModel, onOptionSelected: (optionIndex: Int) -> Unit) {
        binding.tvQuestion.text = "Câu hỏi ${mItem.questionId}"

        itemOptionAdapter.submitList(mItem.options)
        itemOptionAdapter = ItemOptionAdapter().apply {
            onClick { index ->
                onOptionSelected(index)
            }
        }
    }
}