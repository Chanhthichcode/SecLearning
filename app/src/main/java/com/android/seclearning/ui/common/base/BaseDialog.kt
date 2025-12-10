package com.android.seclearning.ui.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.android.seclearning.common.utils.hideNavBarCompact
import com.android.seclearning.common.utils.setupMyDialogBottom
import com.android.seclearning.ui.common.isLiving
import java.lang.IllegalStateException

abstract class BaseDialog<BINDING : ViewBinding> : DialogFragment() {
    private var binding: BINDING? = null

    fun viewBinding() = binding

    abstract fun makeBinding(inflater: LayoutInflater): BINDING

    abstract fun initViewAndData(saveInstanceState: Bundle?, binding: BINDING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) onOpenScreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = makeBinding(layoutInflater)
        return binding!!.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setupMyDialogBottom(gravity = getGravityForDialog())
        return dialog
    }

    protected fun setSizeForDialog(width: Int) {
        if (dialog?.window != null) {
            dialog?.window?.setLayout(
                requireActivity().window.decorView.width * width / 10,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    protected fun setSizeFullForDialog() {
        if (dialog?.window != null) {
            activity?.let {
                dialog?.window?.setLayout(
                    it.window.decorView.width,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    protected fun setSizeWrapForDialog() {
        if (dialog?.window != null) {
            dialog?.window?.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    protected fun setFullScreenDialog() {
        if (dialog?.window != null) {
            dialog?.window?.setLayout(
                requireActivity().window.decorView.width,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
    }

    protected fun setFullScreenStatusBar() {
        if (dialog?.window != null) {
            dialog?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    protected fun onBackPress(showInterAds: () -> Unit) {
        dialog?.setOnCancelListener {
            showInterAds.invoke()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndData(savedInstanceState, binding!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    open fun getGravityForDialog(): Int = Gravity.CENTER

    override fun show(
        fragmentManager: FragmentManager,
        tag: String?
    ) {
        if (!fragmentManager.isLiving()) return
        try {
            val transaction =
                fragmentManager.beginTransaction()
            val prevFragment = fragmentManager.findFragmentByTag(tag)
            if (prevFragment != null) {
                transaction.remove(prevFragment)
                transaction.commitNowAllowingStateLoss()
            }
            /*transaction.addToBackStack(null)*/

            show(transaction, tag)
        } catch (e: IllegalStateException) {
        }
    }

    fun dismissDialog(tag: String?) {
        runCatching { dismissAllowingStateLoss() }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.hideNavBarCompact()
        if (activity?.isFinishing == true || isRemoving && activity?.isChangingConfigurations != true) {
            onScreenEndOfLifecycle()
        }
    }

    open fun onOpenScreen() {}

    open fun onScreenEndOfLifecycle() {}
}