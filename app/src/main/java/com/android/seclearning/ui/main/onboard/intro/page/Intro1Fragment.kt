package com.android.seclearning.ui.main.onboard.intro.page

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import com.android.seclearning.common.EventHelper
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.databinding.FragmentIntro1Binding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.onboard.intro.IntroFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Intro1Fragment : BaseFragment<FragmentIntro1Binding>() {

    companion object {
        fun newInstance() = Intro1Fragment()
    }

    private var firstOpenScreen: Boolean = false

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun makeBinding(inflater: LayoutInflater) = FragmentIntro1Binding.inflate(inflater)

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(this, backPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.remove()
    }

    override fun initViewAndData(saveInstanceState: Bundle?, binding: FragmentIntro1Binding) {
        binding.btnNext.setSafeOnClickScaleEffect {
            EventHelper.post(IntroFragment.EventClickNext())
        }
    }


    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            if (!firstOpenScreen) {
                firstOpenScreen = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
