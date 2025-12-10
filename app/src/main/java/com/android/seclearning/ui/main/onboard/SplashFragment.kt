package com.android.seclearning.ui.main.onboard

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.seclearning.databinding.FragmentSplashBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()

    private var initJob: Job? = null
    private var isResume = false
    private var isHandleNavigationScreenCall = false

    private val backPressedCallback = object :
        OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {}
    }

    override fun makeBinding(inflater: LayoutInflater): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater)
    }

    override fun initViewAndData(saveInstanceState: Bundle?, binding: FragmentSplashBinding) {

        initJob?.cancel()
        initJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(500)
            viewModel.getAppConfig()

            viewModel.appStatusLoaded.observe(viewLifecycleOwner) { loaded ->
                if (loaded == true) {
                    nextScreen()
                }
            }
        }
    }

    override fun onDestroyView() {
        initJob?.cancel()
        super.onDestroyView()
    }

    override fun onResume() {
        isResume = true
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun onPause() {
        isResume = false
        super.onPause()
        backPressedCallback.remove()
    }

    @Synchronized
    private fun handleNavigateScreen() {
        if (!isResume || isHandleNavigationScreenCall) return
        isHandleNavigationScreenCall = true

        try {
            when {
                viewModel.isFirstOpenApp() -> {
                    navigateAnim { NavigationManager.navigateToIntro(parentFragmentManager) }
                }
                !viewModel.isLoggedIn() -> {
                    navigateAnim { NavigationManager.navigateToLogin(parentFragmentManager) }
                }
                !viewModel.isBuildRoadmap() -> {
//                    navigateAnim { NavigationManager.navigateToBuildRoadmap(parentFragmentManager) }
                }
                else -> {
                    navigateAnim { NavigationManager.navigateToMain(parentFragmentManager) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigateAnim(onDone: () -> Unit) {
        onDone.invoke()
    }

    private fun nextScreen() {
        handleNavigateScreen()
    }
}