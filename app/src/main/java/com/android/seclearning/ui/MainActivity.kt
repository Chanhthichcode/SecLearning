package com.android.seclearning.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.seclearning.common.utils.ContextExt.hideKeyboard
import com.android.seclearning.databinding.ActivityMainBinding
import com.android.seclearning.ui.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.let

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModels()
    private var job: Job? = null

    override fun makeBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initViewAndData(saveInstanceState: Bundle?, binding: ActivityMainBinding) {
        if (viewModel.isNavigateToSplash) {
            job = lifecycleScope.launch {
                delay(2000)
                viewModel.isNavigateToSplash = false
            }
            NavigationManager.navigateToSplash(supportFragmentManager)
        } else NavigationManager.navigateToMain(supportFragmentManager)

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let { view ->
                if (view is EditText) {
                    val outRect = Rect()
                    view.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        view.clearFocus()
                        hideKeyboard(view)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}