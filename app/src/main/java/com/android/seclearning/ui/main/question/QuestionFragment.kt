package com.android.seclearning.ui.main.question

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.activityViewModels
import com.android.seclearning.common.utils.marginWithStatusBar
import com.android.seclearning.databinding.FragmentQuestionBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.question.adapter.QuestionPagerAdapter
import com.android.seclearning.ui.main.question.pager.Question3Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>() {
    private val viewModel: QuestionViewModel by activityViewModels()
    private var questionPagerAdapter: QuestionPagerAdapter? = null
    override fun makeBinding(inflater: LayoutInflater): FragmentQuestionBinding {
        return FragmentQuestionBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentQuestionBinding
    ) {
        binding.spaceStatusBar.marginWithStatusBar()

        setupPager(binding)
    }

    private fun setupPager(binding: FragmentQuestionBinding) {
        questionPagerAdapter = QuestionPagerAdapter(
            childFragmentManager,
            lifecycle
        )

        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = questionPagerAdapter
        }

        viewModel.navigatePage.observe(viewLifecycleOwner) { page ->
            binding.viewPager.currentItem = page
        }

        setupTabMediator(binding)
    }

    private fun setupTabMediator(binding: FragmentQuestionBinding) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            binding.tabLayout.getTabAt(i)?.view?.let { tabView ->
                TooltipCompat.setTooltipText(tabView, null)
            }
        }
    }

    @Subscribe
    fun onEvent(event: Question3Fragment.EventClickDone) {
        NavigationManager.navigateToQuiz(parentFragmentManager)
    }
}