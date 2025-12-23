package com.android.seclearning.ui.main.home.lab

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.seclearning.R
import com.android.seclearning.common.utils.boldLabel
import com.android.seclearning.common.utils.boldLabelWithUnderlineUrl
import com.android.seclearning.common.utils.gone
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.OpenLabFrom
import com.android.seclearning.data.model.LabModel
import com.android.seclearning.databinding.ItemLabBinding
import com.android.seclearning.ui.main.home.lab.ItemLabAdapter.ItemLabViewHolder

class ItemLabAdapter :
    ListAdapter<LabModel, ItemLabViewHolder>(LabItemDiffCallback()) {

    private var onCopy: ((String) -> Unit)? = null
    fun onCopy(onClick: (String) -> Unit) {
        this.onCopy = onClick
    }

    private val expandedItems = hashSetOf<Int>()

    inner class ItemLabViewHolder(val binding: ItemLabBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: LabModel, position: Int) {
            binding.apply {
                namePackage.text = item.title
                namePackage.isSelected = true
                imgLab.setImageResource(getPlatformIcon(item.platform))
                val levelValue = when (item.difficulty) {
                    2 -> "Dễ"
                    3 -> "Trung bình"
                    4 -> "Khó"
                    else -> "Không xác định"
                }
                tvLevel.text = boldLabel("Level", levelValue)

                // Danh mục
                tvCategory.text = boldLabel("Danh mục", item.category)

                // Kỹ năng
                tvSkill.text = boldLabel(
                    "Kỹ năng",
                    item.skillTags.joinToString(", ")
                )

                // Mô tả
                tvDescription.text = boldLabel(
                    "Mô tả",
                    item.descriptionDetail
                )

                // Đường dẫn (bold label + underline url)
                tvUrl.text = boldLabelWithUnderlineUrl(
                    "Đường dẫn",
                    item.url
                )
                tvUrl.setSafeOnClickListener {
                    onCopy?.invoke(item.url)
                }
                val isExpanded = expandedItems.contains(item.id)
                layoutDetail.visibility =
                    if (isExpanded) ViewGroup.VISIBLE else ViewGroup.GONE

                icMore.setImageResource(if (isExpanded) R.drawable.ic_up else R.drawable.ic_down)

                layoutTop.setSafeOnClickListener {
                    if (isExpanded) {
                        expandedItems.remove(item.id)
                    } else {
                        expandedItems.add(item.id)
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
            return when (platform) {
                OpenLabFrom.LABTAINER.from -> R.drawable.logo_labtainer
                OpenLabFrom.SEED_LAB.from -> R.drawable.logo_seed_labs
                OpenLabFrom.BLUE_TEAM.from -> R.drawable.logo_blue_team
                OpenLabFrom.CYBER.from -> R.drawable.logo_cyber_defender
                else -> R.drawable.logo_post_swigger
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemLabViewHolder {
        val binding =
            ItemLabBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemLabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemLabViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun onViewRecycled(holder: ItemLabViewHolder) {
        super.onViewRecycled(holder)
        holder.recycled()
    }


    class LabItemDiffCallback : DiffUtil.ItemCallback<LabModel>() {
        override fun areItemsTheSame(oldItem: LabModel, newItem: LabModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LabModel, newItem: LabModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}

