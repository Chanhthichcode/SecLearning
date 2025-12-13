package com.android.seclearning.ui.main.question.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.seclearning.ui.main.question.pager.Question1Fragment
import com.android.seclearning.ui.main.question.pager.Question2Fragment
import com.android.seclearning.ui.main.question.pager.Question3Fragment

class QuestionPagerAdapter(
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    private val fragmentList: List<Fragment> = listOf(
        Question1Fragment(),
        Question2Fragment(),
        Question3Fragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getFragment(position: Int): Fragment = fragmentList[position]
}