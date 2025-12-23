package com.android.seclearning.ui.main.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.seclearning.ui.main.home.main.HomeFragment
import com.android.seclearning.ui.main.home.personal.PersonalFragment
import com.android.seclearning.ui.main.home.lab.LabFragment
import com.android.seclearning.ui.main.home.setting.SettingFragment

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> PersonalFragment()
            2 -> LabFragment()
            else -> SettingFragment()
        }
    }

}