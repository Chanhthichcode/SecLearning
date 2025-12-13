package com.android.seclearning.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.seclearning.R
import com.android.seclearning.common.utils.fadeInDynamic
import com.android.seclearning.common.utils.fadeOutDynamic
import com.android.seclearning.common.utils.gone
import com.android.seclearning.common.utils.visible

class CustomLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var lottieView: LottieAnimationView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_loading_view, this, true)
        lottieView = view.findViewById(R.id.lottie_loading)
    }

    @Synchronized
    fun startLoading(isAnim: Boolean = true) {
//        if (isVisible) return
        this.visible()
        lottieView.playAnimation()
        lottieView.visible()
        if (isAnim) {
            lottieView.clearAnimation()
            lottieView.fadeInDynamic(250)
        } else lottieView.alpha = 1f
    }

    @Synchronized
    fun stopLoading(isAnim: Boolean = true) {
        if (isAnim) {
            lottieView.fadeOutDynamic(150) {
                lottieView.gone()
                this.gone()
                lottieView.pauseAnimation()
            }
        } else {
            lottieView.alpha = 0f
            lottieView.gone()
            this.gone()
            lottieView.pauseAnimation()
        }
    }
}