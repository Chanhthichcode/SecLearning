package com.android.seclearning.ui.main.home.lab

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
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

class ItemLabAdapter :
    ListAdapter<LabModel, ItemLabAdapter.ItemLabViewHolder>(LabItemDiffCallback()) {

    private var onCopy: ((String) -> Unit)? = null
    fun onCopy(onClick: (String) -> Unit) {
        this.onCopy = onClick
    }

    private val expandedItems = hashSetOf<Int>()

    inner class ItemLabViewHolder(
        private val binding: ItemLabBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: LabModel, position: Int) = with(binding) {
            bindBasicInfo(item)
            bindDetailInfo(item)
            bindExpandState(item, position)
        }

        private fun ItemLabBinding.bindBasicInfo(item: LabModel) {
            namePackage.text = item.title
            namePackage.isSelected = true
            imgLab.setImageResource(getPlatformIcon(item.platform))

            tvLevel.text = boldLabel("Level", getLevelText(item.difficulty))
            tvCategory.text = boldLabel("Danh mục", item.category)
            tvSkill.text = boldLabel("Kỹ năng", item.skillTags.joinToString(", "))
            tvDescription.text = boldLabel("Mô tả", item.descriptionDetail)
        }

        private fun ItemLabBinding.bindDetailInfo(item: LabModel) {
            tvUrl.text = boldLabelWithUnderlineUrl("Đường dẫn", item.url)
            tvUrl.setSafeOnClickListener {
                onCopy?.invoke(item.url)
            }
        }

        private fun ItemLabBinding.bindExpandState(item: LabModel, position: Int) {
            val isExpanded = expandedItems.contains(item.id)

            layoutDetail.visibility =
                if (isExpanded) View.VISIBLE else View.GONE

            icMore.setImageResource(
                if (isExpanded) R.drawable.ic_up else R.drawable.ic_down
            )

            layoutTop.setSafeOnClickListener {
                toggleExpand(item.id, position)
            }
        }

        private fun toggleExpand(id: Int, position: Int) {
            if (expandedItems.contains(id)) {
                expandedItems.remove(id)
            } else {
                expandedItems.add(id)
            }
            notifyItemChanged(position)
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
                OpenLabFrom.PORT_SWIGGER.from -> R.drawable.logo_post_swigger
                else -> R.drawable.ic_lab
            }
        }

        private fun getLevelText(difficulty: Int): String {
            return when (difficulty) {
                2 -> "Dễ"
                3 -> "Trung bình"
                4 -> "Khó"
                else -> "Không xác định"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLabViewHolder {
        val binding = ItemLabBinding.inflate(
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
        override fun areItemsTheSame(oldItem: LabModel, newItem: LabModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LabModel, newItem: LabModel): Boolean =
            oldItem == newItem
    }
}
