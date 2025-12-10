package com.android.seclearning.common.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.transition.Transition
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.seclearning.R
import kotlin.let

fun Activity.resetTransitions() {
    window.sharedElementEnterTransition = null
    window.sharedElementReturnTransition = null
    window.sharedElementExitTransition = null
    window.sharedElementReenterTransition = null
}

fun Activity.setShareTransition(transition: Transition) {
    window.sharedElementEnterTransition = transition
    window.sharedElementReturnTransition = transition
    window.sharedElementExitTransition = transition
    window.sharedElementReenterTransition = transition
}

fun Window.myEnableEdgeToEdge(
    lightStatusBar: Boolean = false,
    lightNavigationBar: Boolean = false,
    hideNavigationBar: Boolean = true
) {

    var systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    if (hideNavigationBar) {
        systemUiVisibility =
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    decorView.systemUiVisibility = systemUiVisibility

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setDecorFitsSystemWindows(false)
        insetsController?.let { controller ->
            var appearance = 0
            if (lightStatusBar) {
                appearance = appearance or WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            }
            if (lightNavigationBar) {
                appearance = appearance or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            }
            controller.setSystemBarsAppearance(
                appearance,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        }
    } else {
        var flags = decorView.systemUiVisibility

        if (lightStatusBar) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }

        if (lightNavigationBar) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }

        decorView.systemUiVisibility = flags
    }

    statusBarColor = Color.TRANSPARENT
    navigationBarColor = Color.TRANSPARENT
}

fun Window.setupMyDialogBottom(lightStatusBar: Boolean = false, gravity: Int = Gravity.CENTER) {
    myEnableEdgeToEdge(lightStatusBar = lightStatusBar)
    attributes?.windowAnimations = R.style.AnimationDialog
    setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    setGravity(gravity)
}

fun Window.setupMyDialogFullScreen(lightStatusBar: Boolean = false) {
    myEnableEdgeToEdge(lightStatusBar = lightStatusBar)
    attributes?.windowAnimations = R.style.DialogRequestPermissionTheme
    setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

fun Activity.myEnableEdgeToEdge(
    lightStatusBar: Boolean = false,
    lightNavigationBar: Boolean = false,
    hideNavigationBar: Boolean = true
) {
    window.myEnableEdgeToEdge(lightStatusBar, lightNavigationBar, hideNavigationBar)
}

fun Activity.hideNavigationBar() {
    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
}

fun Activity.showNavigationBar() {
    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
}

fun Activity.hideNavBarNewApi(){
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.hideNavBarCompact(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        hideNavBarNewApi()
    } else {
        hideNavigationBar()
    }
}