package com.android.seclearning.view

import android.content.Context
import android.graphics.Paint
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

            try {
                val fontResId = typedArray.getResourceId(
                    R.styleable.MyTextView_customFontFamily,
                    R.font.my_font_regular
                )

                if (fontResId != -1) {
                    myApp().fontPreloader
                        .getPreloadedFont(fontResId)
                        ?.let { typeface = it }
                }

                val strike = typedArray.getBoolean(
                    R.styleable.MyTextView_strikeThrough,
                    false
                )

                if (strike) {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }

            } finally {
                typedArray.recycle()
            }
        }
    }
}