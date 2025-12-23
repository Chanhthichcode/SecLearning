package com.android.seclearning.ui.main.home.main.learning.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.seclearning.ui.main.home.main.learning.pager.LearningPathFragment
import com.android.seclearning.ui.main.home.main.learning.pager.OverviewFragment
import com.android.seclearning.ui.main.home.main.learning.pager.ResourceFragment

class LearningPagerAdapter(
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    private val fragmentList: List<Fragment> = listOf(
        OverviewFragment(),
        LearningPathFragment(),
        ResourceFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}