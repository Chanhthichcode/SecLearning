package com.android.seclearning.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.android.seclearning.R
import com.android.seclearning.ui.main.home.MainFragment
import com.android.seclearning.ui.main.login.LoginFragment
import com.android.seclearning.ui.main.login.RegisterFragment
import com.android.seclearning.ui.main.onboard.SplashFragment
import com.android.seclearning.ui.main.onboard.intro.IntroFragment
import kotlin.apply
import kotlin.jvm.java

object NavigationManager {
    fun navigateToSplash(manager: FragmentManager) {
        try {
            val fragment = SplashFragment()
            manager.beginTransaction().apply {
                replace(R.id.frame_main, fragment)
                commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigateToMain(manager: FragmentManager) {
        try {
            val fragment = MainFragment().apply {
                arguments = Bundle().apply {}
            }
            manager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.new_fade_in,
                    R.anim.new_fade_out,
                    R.anim.new_fade_in,
                    R.anim.new_fade_out
                )
                replace(R.id.frame_main, fragment)
                commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    fun navigatePolicy(activity: Activity) {
//        postDelayedSkipException {
//            val intent = Intent(activity, PolicyActivity::class.java).apply {
//            }
//            activity.startActivity(intent)
//        }
//    }

    fun backToHome(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
        activity.finish()
    }

    fun navigateToIntro(manager: FragmentManager) {
        try {
            val fragment = IntroFragment()
            manager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.new_fade_in,
                    R.anim.new_fade_out,
                    R.anim.new_fade_in,
                    R.anim.new_fade_out
                )
                replace(R.id.frame_main, fragment)
                commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigateToLogin(manager: FragmentManager) {
        try {
            val fragment = LoginFragment()
            manager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.new_fade_in,
                    R.anim.new_fade_out,
                    R.anim.new_fade_in,
                    R.anim.new_fade_out
                )
                replace(R.id.frame_main, fragment)
                commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigateToRegister(manager: FragmentManager) {
        try {
            val fragment = RegisterFragment()
            manager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.new_fade_in,
                    R.anim.new_fade_out,
                    R.anim.new_fade_in,
                    R.anim.new_fade_out
                )
                replace(R.id.frame_main, fragment)
                commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}