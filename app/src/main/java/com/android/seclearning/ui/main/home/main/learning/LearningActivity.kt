package com.android.seclearning.ui.main.home.main.learning

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.TooltipCompat
import com.android.seclearning.Constants.OPEN_DETAIL_FROM
import com.android.seclearning.R
import com.android.seclearning.common.utils.marginWithStatusBar
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.databinding.ActivityLearningBinding
import com.android.seclearning.ui.common.base.BaseActivity
import com.android.seclearning.ui.main.home.main.learning.adapter.LearningPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LearningActivity : BaseActivity<ActivityLearningBinding>() {

    private val viewModel: LearningViewModel by viewModels()

    private lateinit var learningPagerAdapter: LearningPagerAdapter

    override fun makeBinding(inflater: LayoutInflater): ActivityLearningBinding {
        return ActivityLearningBinding.inflate(layoutInflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: ActivityLearningBinding
    ) {
        binding.spaceStatusBar.marginWithStatusBar()

        viewModel.openDetailFrom =
            intent.getStringExtra(OPEN_DETAIL_FROM).toString()

        binding.btnBack.setSafeOnClickListener {
            doFinish()
        }

        setupPager(binding)

        binding.nameCategory.isSelected = true

        when (viewModel.fromDetailPackage()) {
            OpenDetailFrom.SOC -> {
                setCategory("SOC Analyst")
            }
            OpenDetailFrom.WEB -> {
                setCategory("Web Pentester")
            }
            OpenDetailFrom.NETWORK -> {
                setCategory("Network Security / Red Team")
            }
            OpenDetailFrom.MALWARE -> {
                setCategory("Malware Analyst")
            }
            else -> {
                setCategory("Digital Forensics & Incident Response")
            }
        }
    }

    private fun setCategory(category: String){
        viewBinding()?.nameCategory?.text = category
    }

    private fun setupPager(binding: ActivityLearningBinding) = with(binding) {
        learningPagerAdapter = LearningPagerAdapter(
            supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = learningPagerAdapter
        setupTabMediator(binding)
    }

    private fun setupTabMediator(binding: ActivityLearningBinding) = with(binding) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.title_overview)
                1 -> getString(R.string.title_learning_path)
                else -> getString(R.string.title_resource)
            }
        }.attach()

        for (i in 0 until tabLayout.tabCount) {
            tabLayout.getTabAt(i)?.view?.let {
                TooltipCompat.setTooltipText(it, null)
            }
        }
    }
}