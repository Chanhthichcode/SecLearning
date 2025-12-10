package com.android.seclearning.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.android.seclearning.R
import com.android.seclearning.myApp
import kotlin.let

class MyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView)

            var fontResId = typedArray.getResourceId(R.styleable.MyTextView_customFontFamily, R.font.my_font_regular)

            if (fontResId != -1) {
                val preloadedFont = myApp().fontPreloader.getPreloadedFont(fontResId)
                preloadedFont?.let { typeface = it }
            }else
                typedArray.recycle()
        }
    }
}