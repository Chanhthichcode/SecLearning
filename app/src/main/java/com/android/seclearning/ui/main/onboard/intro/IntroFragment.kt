package com.android.seclearning.ui.main.onboard.intro

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.android.seclearning.common.utils.myEnableEdgeToEdge
import com.android.seclearning.databinding.FragmentIntroBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.onboard.intro.page.Intro1Fragment
import com.android.seclearning.ui.main.onboard.intro.page.Intro2Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import kotlin.apply
import kotlin.let

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>() {

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    private lateinit var introPagerAdapter: IntroPagerAdapter

    private var arrayType = mutableListOf<IntroType>()
    private fun getType(position: Int) = arrayType[position]

    private val viewPagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewBinding()?.apply {
                when (getType(position)) {
                    IntroType.INTRO_ONE,
                    IntroType.INTRO_TWO -> {}
                }
            }
        }
    }

    override fun makeBinding(inflater: LayoutInflater) = FragmentIntroBinding.inflate(inflater)

    override fun initViewAndData(saveInstanceState: Bundle?, binding: FragmentIntroBinding) {
        introPagerAdapter = IntroPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        ).apply {
            addFragment(Intro1Fragment.newInstance())
            arrayType.add(IntroType.INTRO_ONE)
            addFragment(Intro2Fragment.newInstance())
            arrayType.add(IntroType.INTRO_TWO)
        }
        binding.viewPager.adapter = introPagerAdapter
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.registerOnPageChangeCallback(viewPagerChangeCallback)
    }

    override fun onStart() {
        super.onStart()
        activity?.myEnableEdgeToEdge(lightNavigationBar = true)
    }

    override fun onDestroyView() {
        viewBinding()?.viewPager?.unregisterOnPageChangeCallback(viewPagerChangeCallback)
        introPagerAdapter.release()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.remove()
    }

    class EventClickNext

    @Subscribe
    fun onEvent(event: EventClickNext) {
        viewBinding()?.let { binding ->
            binding.viewPager.currentItem += 1
            when (getType(binding.viewPager.currentItem)) {
                IntroType.INTRO_ONE,
                IntroType.INTRO_TWO -> {
                }

            }
        }
    }

    @Subscribe
    fun onEvent(event: Intro2Fragment.EventClickDone) {
        NavigationManager.navigateToLogin(parentFragmentManager)
    }
}