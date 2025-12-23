package com.android.seclearning.ui.main.question.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.R
import com.android.seclearning.databinding.ItemOptionQuestionBinding

class ItemOptionAdapter :
    ListAdapter<String, ItemOptionAdapter.OptionVH>(Diff()) {

    private var selectedIndex = -1

    var onClick: ((Int) -> Unit)? = null

    fun setSelected(index: Int) {
        val old = selectedIndex
        selectedIndex = index
        if (old != -1) notifyItemChanged(old)
        if (index != -1) notifyItemChanged(index)
    }

    inner class OptionVH(val binding: ItemOptionQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, position: Int) {
            val key = item.substringBefore(".")
            val content = item.substringAfter(".").trim()

            binding.tvKey.text = key
            binding.tvContent.text = content

            val selected = position == selectedIndex

            binding.tvKey.setBackgroundResource(
                if (selected) R.drawable.bg_option_selected
                else R.drawable.bg_option_unselected
            )

            binding.tvKey.setTextColor(
                if (selected) Color.WHITE else Color.BLACK
            )

            binding.root.setOnClickListener {
                onClick?.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionVH {
        val binding = ItemOptionQuestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OptionVH(binding)
    }

    override fun onBindViewHolder(holder: OptionVH, position: Int) {
        holder.bind(getItem(position), position)
    }

    class Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(o: String, n: String) = o == n
        override fun areContentsTheSame(o: String, n: String) = o == n
    }
}

