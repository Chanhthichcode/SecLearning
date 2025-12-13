package com.android.seclearning.ui.main.question.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.R
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.databinding.ItemOptionQuestionBinding
import com.android.seclearning.ui.main.question.adapter.ItemOptionAdapter.ItemOptionHomeViewHolder
import java.util.concurrent.Executors

class ItemOptionAdapter :
    ListAdapter<String, ItemOptionHomeViewHolder>(
        AsyncDifferConfig.Builder(
            QuizItemDiffCallback()
        ).setBackgroundThreadExecutor(Executors.newSingleThreadExecutor()).build()
    ) {

    private var selectedPosition = RecyclerView.NO_POSITION

    private var onClick: ((Int) -> Unit)? = null
    fun onClick(onClick: (Int) -> Unit) {
        this.onClick = onClick
    }

    inner class ItemOptionHomeViewHolder(val binding: ItemOptionQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, position: Int) {
            binding.root.setSafeOnClickListener {
                onClick?.invoke(position)
            }
            val key = item.substringBefore(".")
            val content = item.substringAfter(".").trim()

            binding.tvKey.text = key
            binding.tvContent.text = content

            val isSelected = position == selectedPosition

            binding.tvKey.setBackgroundResource(
                if (isSelected) R.drawable.bg_option_selected
                else R.drawable.bg_option_unselected
            )

            binding.tvKey.setTextColor(
                if (isSelected) Color.WHITE
                else Color.BLACK
            )

            binding.root.setOnClickListener {
                val oldPos = selectedPosition
                selectedPosition = position

                notifyItemChanged(oldPos)
                notifyItemChanged(position)

                onClick?.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemOptionHomeViewHolder {
        val binding =
            ItemOptionQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemOptionHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemOptionHomeViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class QuizItemDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String, newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }

}