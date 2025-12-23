package com.android.seclearning.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.android.seclearning.Constants.OPEN_DETAIL_FROM
import com.android.seclearning.Constants.OPEN_LAB_FROM
import com.android.seclearning.Logger
import com.android.seclearning.R
import com.android.seclearning.common.utils.postDelayedSkipException
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.data.enums.OpenLabFrom
import com.android.seclearning.ui.common.isLiving
import com.android.seclearning.ui.main.home.MainFragment
import com.android.seclearning.ui.main.home.lab.LabDetailActivity
import com.android.seclearning.ui.main.home.main.learning.LearningActivity
import com.android.seclearning.ui.main.home.personal.PathDetailActivity
import com.android.seclearning.ui.main.login.ForgotPasswordFragment
import com.android.seclearning.ui.main.login.LoginFragment
import com.android.seclearning.ui.main.login.RegisterFragment
import com.android.seclearning.ui.main.onboard.SplashFragment
import com.android.seclearning.ui.main.onboard.intro.IntroFragment
import com.android.seclearning.ui.main.question.QuestionFragment
import com.android.seclearning.ui.main.question.QuizTestFragment

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

    fun navigateToForgetPassword(manager: FragmentManager) {
        try {
            val fragment = ForgotPasswordFragment()
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

    fun navigateToQuestion(manager: FragmentManager) {
        try {
            val fragment = QuestionFragment()
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

    fun navigateToQuiz(manager: FragmentManager, ) {
        try {
            val fragment = QuizTestFragment()
            manager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.new_fade_in,
                    R.anim.new_fade_out,
                    R.anim.new_fade_in,
                    R.anim.new_fade_out
                )
                replace(R.id.frame_main, fragment)
                addToBackStack("QuizTestFragment")
                commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigateToLearningActivity(
        activity: Activity,
        detailFrom: OpenDetailFrom
    ) {
        if (!activity.isLiving()) return
        try {
            val intent = Intent(activity, LearningActivity::class.java).apply {
                putExtra(OPEN_DETAIL_FROM, detailFrom.from)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            Logger.logAction("ERROR: ${e.message}")
            e.printStackTrace()
        }
    }

    fun navigateToPathDetailActivity(
        activity: Activity,
        career: String
    ) {
        postDelayedSkipException {
            if (!activity.isLiving()) return@postDelayedSkipException
            val intent = Intent(activity, PathDetailActivity::class.java)
            intent.putExtra("career", career)
            activity.startActivity(intent)
        }
    }


    fun navigateToLabDetailActivity(
        activity: Activity,
        detailFrom: OpenLabFrom
    ) {
        if (!activity.isLiving()) return
        try {
            val intent = Intent(activity, LabDetailActivity::class.java).apply {
                putExtra(OPEN_LAB_FROM, detailFrom.from)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            Logger.logAction("ERROR: ${e.message}")
            e.printStackTrace()
        }
    }
}