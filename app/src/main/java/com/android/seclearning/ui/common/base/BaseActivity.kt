package com.android.seclearning.ui.common.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.VANILLA_ICE_CREAM
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.android.seclearning.AppConfig
import com.android.seclearning.R
import com.android.seclearning.common.EventHelper
import com.android.seclearning.common.display.DisplayManager
import com.android.seclearning.common.utils.MyInternet
import com.android.seclearning.common.utils.NetworkUtils
import com.android.seclearning.common.utils.hideNavBarNewApi
import com.android.seclearning.common.utils.myEnableEdgeToEdge
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.ui.common.isLiving
import com.google.android.material.snackbar.Snackbar
import com.android.seclearning.ui.dialog.NoInternetDialog

abstract class BaseActivity<BINDING : ViewBinding> : AppCompatActivity() {
    private var binding: BINDING? = null
    private var isAndroid15Above = SDK_INT >= VANILLA_ICE_CREAM

    fun viewBinding() = binding

    abstract fun makeBinding(inflater: LayoutInflater): BINDING

    abstract fun initViewAndData(saveInstanceState: Bundle?, binding: BINDING)

    open fun handleBackPress() = false

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            doFinish()
        }
    }

    open fun doFinish() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = makeBinding(layoutInflater)
        setContentView(binding!!.root)
        initViewAndData(savedInstanceState, binding!!)
        if (savedInstanceState == null) onOpenScreen()
        EventHelper.register(this)
    }

    open fun onOpenScreen() {}

    override fun onResume() {
        super.onResume()
        edgeToEdgeActivity()
        if (handleBackPress())
            onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    fun edgeToEdgeActivity() {
        if (isAndroid15Above) {
            enableEdgeToEdge()
            window.isNavigationBarContrastEnforced = false
            hideNavBarNewApi()
        } else {
            myEnableEdgeToEdge(lightStatusBar = true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (handleBackPress())
            backPressedCallback.remove()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventHelper.register(this)
        binding = null
        if (isFinishing) {
            onScreenEndOfLifeCycle()
        }
    }

    open fun onScreenEndOfLifeCycle() {}

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppConfig.getLocalizedContext(newBase))
    }

    private val noInternetDialog by lazy {
        NoInternetDialog()
    }

    private fun observerInternetConnection() {
        if (!isLiving()) return
        MyInternet(this).observe(this) { isConnected ->
            isConnected?.let {
                if (it) {
                    if (noInternetDialog.isAdded) {
                        showSnackBarInternetConnected(binding!!.root)
                        noInternetDialog.dismissDialog(NoInternetDialog.TAG)
                    }
                } else handleShowDialogInternet()
            }
        }
    }

    fun handleShowDialogInternet() {
        if (!isLiving()) return
        val existDialog = supportFragmentManager.findFragmentByTag(NoInternetDialog.TAG)
        if (noInternetDialog.isAdded && existDialog != null) return

        noInternetDialog.setOnClickViewSettings {
            NetworkUtils.openNetworkSetting(this@BaseActivity)
        }
        noInternetDialog.setOnClickBack {
            EventHelper.post(EventGoToMyFiles())
        }
        runCatching {
            noInternetDialog.show(supportFragmentManager, NoInternetDialog.TAG)
        }
    }

    class EventGoToMyFiles

    @SuppressLint("RestrictedApi")
    private fun showSnackBarInternetConnected(view: View) {
        val snackBar = Snackbar.make(view, "", 3000)
        val customSnackView =
            LayoutInflater.from(this).inflate(R.layout.layout_toast_internet_connected, null)
        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackBar.view.setBackgroundResource(R.drawable.bg_toast_internet_connected)
        snackBar.view.translationY = -(DisplayManager.dpToPx(this, 50f).toFloat())
        val layoutParams = snackBarLayout.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = DisplayManager.dpToPx(this, 16f)
        layoutParams.marginEnd = DisplayManager.dpToPx(this, 16f)
        snackBarLayout.layoutParams = layoutParams
        snackBarLayout.addView(customSnackView, 0)
        snackBar.show()
    }

    fun View.setSafeInternetOnClick(onSafeClick: (View) -> Unit) {
        setSafeOnClickListener {
            if (!isLiving()) return@setSafeOnClickListener
            if (!NetworkUtils.isConnected()) {
                handleShowDialogInternet()
            } else onSafeClick.invoke(this)
        }
    }
}