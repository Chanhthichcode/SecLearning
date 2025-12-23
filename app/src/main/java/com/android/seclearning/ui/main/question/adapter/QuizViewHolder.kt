package com.android.seclearning.ui.main.question.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.data.model.QuizModel
import com.android.seclearning.databinding.ItemQuizQuestionBinding

class QuizViewHolder(
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
        item: QuizModel,
        position: Int,
        onOptionSelected: (questionId: Int, optionIndex: Int) -> Unit
    ) {
        binding.tvQuestion.text = "Câu ${position + 1}: "
        binding.tvContent.text = item.question

        optionAdapter.submitList(item.options)
        optionAdapter.setSelected(item.selectedOption)

        optionAdapter.onClick = { index ->
            onOptionSelected(item.id, index)
        }

    }
}
