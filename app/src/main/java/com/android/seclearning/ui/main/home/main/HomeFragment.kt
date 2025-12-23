package com.android.seclearning.ui.main.home.main

import android.os.Bundle
import android.view.LayoutInflater
import com.android.seclearning.appRepository
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.databinding.FragmentHomeBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun makeBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentHomeBinding
    ) {
        binding.title5.isSelected = true

        binding.bannerSoc.setSafeOnClickScaleEffect() {
            activity?.run {
                NavigationManager.navigateToLearningActivity(this, OpenDetailFrom.SOC)
            }
        }

        binding.bannerWeb.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLearningActivity(this, OpenDetailFrom.WEB)
            }
        }

        binding.bannerDfir.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLearningActivity(this, OpenDetailFrom.DFIR)
            }
        }

        binding.bannerMalware.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLearningActivity(this, OpenDetailFrom.MALWARE)
            }
        }

        binding.bannerNetwork.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLearningActivity(this, OpenDetailFrom.NETWORK)
            }
        }
    }
}