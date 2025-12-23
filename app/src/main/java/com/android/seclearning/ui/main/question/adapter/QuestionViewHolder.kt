package com.android.seclearning.ui.main.question.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.databinding.ItemQuizQuestionBinding

class QuestionViewHolder(
    private val binding: ItemQuizQuestionBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val optionAdapter = ItemOptionAdapter()

    init {
        binding.rcQuiz.apply {
            adapter = optionAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(
        item: QuestionModel,
        onOptionSelected: (questionId: Int, optionIndex: Int) -> Unit
    ) {
        binding.tvQuestion.text = "Câu ${item.questionId}"
        binding.tvContent.text = item.question

        optionAdapter.submitList(item.options)
        optionAdapter.setSelected(item.selectedOption)

        optionAdapter.onClick = { index ->
            onOptionSelected(item.questionId, index)
        }

    }
}
