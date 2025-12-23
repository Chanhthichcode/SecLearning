package com.android.seclearning.ui.main.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.doOnPreDraw
import androidx.viewpager2.widget.ViewPager2
import com.android.seclearning.R
import com.android.seclearning.common.utils.marginWithStatusBar
import com.android.seclearning.common.utils.myEnableEdgeToEdge
import com.android.seclearning.common.utils.setColor
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.TabHomeType
import com.android.seclearning.databinding.FragmentMainBinding
import com.android.seclearning.myApp
import com.android.seclearning.ui.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val mainPagerAdapter: MainPagerAdapter by lazy {
        MainPagerAdapter(childFragmentManager, lifecycle)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewBinding()?.let { binding ->
                if (binding.viewPager.currentItem != TabHomeType.HOME.type) {
                    updateTabState(TabHomeType.HOME)
                    binding.viewPager.currentItem = TabHomeType.HOME.type
                    return
                } else activity?.finish()
            }
        }
    }

    private val viewPagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            when (position) {
                TabHomeType.HOME.type -> updateTabState(TabHomeType.HOME)
                TabHomeType.PERSONAL.type -> updateTabState(TabHomeType.PERSONAL)
                TabHomeType.LIBRARY.type -> updateTabState(TabHomeType.LIBRARY)
                TabHomeType.SETTING.type -> updateTabState(TabHomeType.SETTING)
                else -> {}
            }
        }
    }

    override fun makeBinding(inflater: LayoutInflater): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater)
    }

    override fun initViewAndData(saveInstanceState: Bundle?, binding: FragmentMainBinding) {
        myApp().isEnterHome = true
        binding.spaceStatusBar.marginWithStatusBar()

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = mainPagerAdapter
        binding.viewPager.offscreenPageLimit = mainPagerAdapter.itemCount - 1
        binding.viewPager.registerOnPageChangeCallback(viewPagerChangeCallback)
        initListener()

        binding.layoutBottomView.doOnPreDraw {
            binding.indicatorView.layoutParams.width = binding.tabHome.width
            binding.indicatorView.requestLayout()
        }

        ViewCompat.setOnApplyWindowInsetsListener(requireActivity().window.decorView) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

            if (!imeVisible) {
                view?.post {
                    hideSystemNavigationBar()
                }
            }
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.myEnableEdgeToEdge(lightStatusBar = true)
    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.remove()
    }

    override fun onDestroyView() {
        viewBinding()?.viewPager?.run {
            adapter = null
            unregisterOnPageChangeCallback(viewPagerChangeCallback)
        }
        super.onDestroyView()
    }

    private fun initListener() {
        viewBinding()?.let { binding ->
            binding.tabHome.setSafeOnClickListener {
                updateTabState(TabHomeType.HOME)
                binding.viewPager.currentItem = TabHomeType.HOME.type
                setTextTitle(TabHomeType.HOME)
            }

            binding.tabMyPath.setSafeOnClickListener {
                updateTabState(TabHomeType.PERSONAL)
                binding.viewPager.currentItem = TabHomeType.PERSONAL.type
                setTextTitle(TabHomeType.PERSONAL)
            }

            binding.tabLibrary.setSafeOnClickListener {
                updateTabState(TabHomeType.LIBRARY)
                binding.viewPager.currentItem = TabHomeType.LIBRARY.type
                setTextTitle(TabHomeType.LIBRARY)
            }

            binding.tabSetting.setSafeOnClickListener {
                updateTabState(TabHomeType.SETTING)
                binding.viewPager.currentItem = TabHomeType.SETTING.type
                setTextTitle(TabHomeType.SETTING)
            }
        }
    }

    private fun setTextTitle(tabType : TabHomeType){
        viewBinding()?.txtTitle?.text = when(tabType){
            TabHomeType.HOME -> getString(R.string.app_name)
            TabHomeType.PERSONAL -> getString(R.string.title_home_me)
            TabHomeType.LIBRARY -> getString(R.string.title_home_library)
            TabHomeType.SETTING -> getString(R.string.title_home_setting)
            else -> getString(R.string.app_name)
        }
    }

    private fun updateTabState(tabSelected: TabHomeType) {
        viewBinding()?.let { binding ->
            val selectedImage = when (tabSelected) {
                TabHomeType.HOME -> R.drawable.ic_home_selected
                TabHomeType.PERSONAL -> R.drawable.ic_path_selected
                TabHomeType.LIBRARY -> R.drawable.ic_library_selected
                TabHomeType.SETTING -> R.drawable.ic_setting_selected
            }
            binding.imgHome.setImageResource((if (tabSelected == TabHomeType.HOME) selectedImage else R.drawable.ic_home))
            setTextTabHome(
                selectedTab = tabSelected == TabHomeType.HOME,
                textView = binding.titleHome
            )

            binding.imgMyPath.setImageResource((if (tabSelected == TabHomeType.PERSONAL) selectedImage else R.drawable.ic_path))
            setTextTabHome(
                selectedTab = tabSelected == TabHomeType.PERSONAL,
                textView = binding.titleMyPath
            )

            binding.imgLibrary.setImageResource((if (tabSelected == TabHomeType.LIBRARY) selectedImage else R.drawable.ic_library))
            setTextTabHome(
                selectedTab = tabSelected == TabHomeType.LIBRARY,
                textView = binding.titleSetting
            )

            binding.imgSetting.setImageResource((if (tabSelected == TabHomeType.SETTING) selectedImage else R.drawable.ic_setting))
            setTextTabHome(
                selectedTab = tabSelected == TabHomeType.SETTING,
                textView = binding.titleSetting
            )
            val index = when (tabSelected) {
                TabHomeType.HOME -> 0
                TabHomeType.PERSONAL -> 1
                TabHomeType.LIBRARY -> 2
                TabHomeType.SETTING -> 3
            }
            moveIndicatorToTab(index, binding)
        }
    }

    @SuppressLint("ResourceType")
    private fun setTextTabHome(selectedTab: Boolean, textView: TextView) {
        if (selectedTab) textView.setColor(getString(R.color.colorPrimary))
        else textView.setColor(getString(R.color.black))
    }

    private fun moveIndicatorToTab(index: Int, binding: FragmentMainBinding) {
        val tabWidth = binding.tabHome.width
        val translationX =  tabWidth * index

        binding.indicatorView.animate()
            .translationX(translationX.toFloat())
            .setDuration(200)
            .start()
    }

    private fun hideSystemNavigationBar() {
        val window = requireActivity().window

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

//    @Subscribe
//    fun onEvent(event: PersonalFragment.EventBackToHome) {
//        updateTabState(TabHomeType.HOME)
//        viewBinding()?.viewPager?.currentItem = TabHomeType.HOME.type
//    }
}