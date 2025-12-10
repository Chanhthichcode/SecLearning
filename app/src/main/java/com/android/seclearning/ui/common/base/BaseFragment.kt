package com.android.seclearning.ui.common.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.android.seclearning.AppConfig
import com.android.seclearning.appRepository
import com.android.seclearning.common.EventHelper
import com.android.seclearning.common.utils.NetworkUtils
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.ui.common.isLiving
import kotlinx.coroutines.launch
import kotlin.jvm.javaClass
import kotlin.let
import kotlin.math.abs

abstract class BaseFragment<BINDING : ViewBinding> : Fragment() {

    private var isUserScrolling = false
    private var lastFirstVisibleItem = 0
    var numberItemScrolled = 0

    open fun getRecyclerView(): RecyclerView? = null

    private var binding: BINDING? = null

    fun viewBinding() = binding

    abstract fun makeBinding(inflater: LayoutInflater): BINDING


    abstract fun initViewAndData(saveInstanceState: Bundle?, binding: BINDING)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = makeBinding(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndData(savedInstanceState, binding!!)
        EventHelper.register(this)
    }

    override fun onDestroyView() {
        binding = null
        EventHelper.unregister(this)
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(AppConfig.getLocalizedContext(context))
    }

    override fun onDestroy() {
        super.onDestroy()
        val isParentRemoving = parentFragment?.isRemoving == true
    }

    fun View.setSafeInternetOnClick(onSafeClick: (View) -> Unit) {
        setSafeOnClickListener {
            if (!NetworkUtils.isConnected()) {
                (activity as? BaseActivity<*>)?.handleShowDialogInternet()
            } else onSafeClick.invoke(this)
        }
    }

    fun View.setSafeInternetScaleEffectOnClick(onSafeClick: (View) -> Unit) {
        setSafeOnClickScaleEffect {
            if (!isLiving()) return@setSafeOnClickScaleEffect
            if (!NetworkUtils.isConnected()) {
                (activity as? BaseActivity<*>)?.handleShowDialogInternet()
            } else onSafeClick.invoke(this)
        }
    }


    fun resetNumberItemScrolled(recyclerView: RecyclerView) {
        if (canResetNumberItemScrolled) {
            numberItemScrolled = 0
            (recyclerView.layoutManager as? LinearLayoutManager)?.let {
                lastFirstVisibleItem = it.findFirstVisibleItemPosition()
            }
        }
        canResetNumberItemScrolled = true
    }

    fun onSafeInternetCheck(onSafeClick: () -> Unit) {
        if (!isLiving()) return
        if (!NetworkUtils.isConnected()) {
            (activity as? BaseActivity<*>)?.handleShowDialogInternet()
        } else onSafeClick.invoke()
    }

    companion object {
        var canResetNumberItemScrolled = true
    }
}