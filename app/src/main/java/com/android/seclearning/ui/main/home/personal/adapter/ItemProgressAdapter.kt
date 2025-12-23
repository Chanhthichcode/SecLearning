package com.android.seclearning.ui.main.home.personal.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.common.utils.boldLabel
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.data.model.ProgressModel
import com.android.seclearning.databinding.ItemLearningPathBinding

class ItemProgressAdapter :
    ListAdapter<ProgressModel, ItemProgressAdapter.ItemProgressViewHolder>(ProgressItemDiffCallback()) {

    private var onClickItem: ((String) -> Unit)? = null
    fun onClickItem(onClick: (String) -> Unit) {
        this.onClickItem = onClick
    }

    inner class ItemProgressViewHolder(val binding: ItemLearningPathBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ProgressModel, position: Int) {
            binding.apply {
                tvTitle.text = setTitle(item.career)
                tvTitle.isSelected = true
                val progressPercent = item.completedItems.size * 100f / 12

                tvProgressPercent.text = boldLabel(
                    "Tiến độ",
                    "%.1f".format(progressPercent) + "%"
                )

                root.setSafeOnClickListener {
                    onClickItem?.invoke(item.career)
                }
            }
        }

        fun setTitle(career: String?): String {
            return when (career) {
                OpenDetailFrom.WEB.from -> "Lộ trình Web Pentester"
                OpenDetailFrom.SOC.from -> "Lộ trình SOC Analyst"
                OpenDetailFrom.DFIR.from -> "Lộ trình Digital Forensics & Incident Response"
                OpenDetailFrom.NETWORK.from -> "Lộ trình Network Security"
                else -> "Lộ trình Malware Analyst"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemProgressViewHolder {
        val binding =
            ItemLearningPathBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemProgressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemProgressViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class ProgressItemDiffCallback : DiffUtil.ItemCallback<ProgressModel>() {
        override fun areItemsTheSame(oldItem: ProgressModel, newItem: ProgressModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProgressModel, newItem: ProgressModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}