package com.android.seclearning.ui.main.home.main.learning.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.Logger
import com.android.seclearning.R
import com.android.seclearning.common.utils.CopyPasteManager
import com.android.seclearning.common.utils.boldLabel
import com.android.seclearning.common.utils.boldLabelWithUnderlineUrl
import com.android.seclearning.common.utils.gone
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.CategoryResourceType
import com.android.seclearning.data.model.ResourceModel
import com.android.seclearning.databinding.ItemLabBinding

class ItemResourceAdapter :
    ListAdapter<ResourceModel, ItemResourceAdapter.ItemResourceViewHolder>(ResourceItemDiffCallback()) {

    private var onCopy: ((String) -> Unit)? = null
    fun onCopy(onClick: (String) -> Unit) {
        this.onCopy = onClick
    }

    private val expandedItems = hashSetOf<Int>()

    inner class ItemResourceViewHolder(val binding: ItemLabBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ResourceModel, position: Int) {
            binding.apply {
                namePackage.text = item.title
                namePackage.isSelected = true
                imgLab.setImageResource(getPlatformIcon(item.category))
                tvLevel.text = boldLabel("Level", item.level)

                // Danh mục
                tvCategory.text = boldLabel("Danh mục", item.category)

                // Kỹ năng
                tvSkill.text = boldLabel(
                    "Ngôn ngữ",
                    item.language
                )

                // Mô tả
                tvDescription.text = boldLabel(
                    "Mô tả",
                    item.notes
                )

                // Đường dẫn (bold label + underline url)
                tvUrl.text = boldLabelWithUnderlineUrl(
                    "Đường dẫn",
                    item.url
                )
                tvUrl.setSafeOnClickListener {
                    onCopy?.invoke(item.url)
                }
                val isExpanded = expandedItems.contains(position)
                layoutDetail.visibility =
                    if (isExpanded) ViewGroup.VISIBLE else ViewGroup.GONE

                icMore.setImageResource(if (isExpanded) R.drawable.ic_up else R.drawable.ic_down)

                layoutTop.setSafeOnClickListener {
                    if (isExpanded) {
                        expandedItems.remove(position)
                    } else {
                        expandedItems.add(position)
                    }
                    notifyItemChanged(position)
                }
            }
        }

        fun recycled() {
            binding.layoutDetail.gone()
            binding.icMore.setImageResource(R.drawable.ic_down)
        }

        fun getPlatformIcon(platform: String?): Int {
            Logger.d("getPlatformIcon", "platform=$platform")
            return when (platform) {
                CategoryResourceType.LEARNING_PATH.from -> R.drawable.ic_learning_path
                CategoryResourceType.CHANNEL.from, CategoryResourceType.PLAYLIST.from, CategoryResourceType.VIDEO.from -> R.drawable.ic_youtube
                else -> R.drawable.ic_document
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemResourceViewHolder {
        val binding =
            ItemLabBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemResourceViewHolder, position: Int) {
        holder.bind(getItem(position), position)
        
    }

    override fun onViewRecycled(holder: ItemResourceViewHolder) {
        super.onViewRecycled(holder)
        holder.recycled()
    }


    class ResourceItemDiffCallback : DiffUtil.ItemCallback<ResourceModel>() {
        override fun areItemsTheSame(oldItem: ResourceModel, newItem: ResourceModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResourceModel, newItem: ResourceModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}

