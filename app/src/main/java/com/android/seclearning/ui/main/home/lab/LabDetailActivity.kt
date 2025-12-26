package com.android.seclearning.ui.main.home.lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.seclearning.Constants.EXTRA_ORIGIN
import com.android.seclearning.Constants.OPEN_LAB_FROM
import com.android.seclearning.R
import com.android.seclearning.common.utils.marginWithStatusBar
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.OpenLabFrom
import com.android.seclearning.databinding.ActivityLabDetailBinding
import com.android.seclearning.ui.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LabDetailActivity : BaseActivity<ActivityLabDetailBinding>() {

    private val viewModel: LabViewModel by viewModels()
    private var labAdapter: ItemLabAdapter? = null

    override fun makeBinding(inflater: LayoutInflater): ActivityLabDetailBinding {
        return ActivityLabDetailBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: ActivityLabDetailBinding
    ) {
        binding.spaceStatusBar.marginWithStatusBar()

        viewModel.openLabFrom =
            intent.getStringExtra(OPEN_LAB_FROM).orEmpty()


        setupCategory()
        setupRecyclerView()
        observeViewModel()

        viewModel.loadLabs()

        binding.btnBack.setSafeOnClickListener {
            doFinish()
        }
    }

    private fun setupCategory() {
        when (viewModel.fromDetailLab()) {
            OpenLabFrom.SEED_LAB -> setCategory("SEED Labs")
            OpenLabFrom.LABTAINER -> setCategory("Labtainer")
            OpenLabFrom.PORT_SWIGGER -> setCategory("Port Swigger")
            OpenLabFrom.BLUE_TEAM -> setCategory("Blue Team Labs")
            OpenLabFrom.CYBER -> setCategory("Cyber Defenders")
            else -> setCategory("New Labs")
        }
    }

    private fun setupRecyclerView() {
        labAdapter = ItemLabAdapter().apply {
            onCopy { text ->
                copyText("lab", text)
            }
        }
        viewBinding()?.rcLab?.apply {
            layoutManager = LinearLayoutManager(this@LabDetailActivity)
            adapter = labAdapter
            setHasFixedSize(true)
        }
    }


    private fun observeViewModel() {
        viewModel.labList.observe(this) { labs ->
            val origin = intent.getStringExtra(EXTRA_ORIGIN)

            if (origin == "origin_path_detail") {
                val maxPossible = if (labs.size > 20) 20 else labs.size
                val randomSize = (6..maxPossible).random()

                val randomLabs = labs.shuffled().take(randomSize)
                labAdapter?.submitList(randomLabs)
                viewBinding()?.tvCount?.text = "${randomLabs.size} bài Lab"
            } else {
                labAdapter?.submitList(labs)
            }
        }

        viewModel.count.observe(this) { count ->
            val origin = intent.getStringExtra(EXTRA_ORIGIN)
            if (origin != "origin_path_detail") {
                viewBinding()?.tvCount?.text = "$count bài Lab"
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            viewBinding()?.loading?.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setCategory(category: String) {
        viewBinding()?.nameCategory?.text = category
    }

    fun copyText(label: String, text: String) {
        viewModel.copyText(label, text)
        this.let { ctx ->
            Toast.makeText(ctx, getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }
    }
}

