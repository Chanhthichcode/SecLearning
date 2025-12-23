package com.android.seclearning.view;

import android.content.Context;
import android.util.AttributeSet;

public class ImageView1v1 extends androidx.appcompat.widget.AppCompatImageView {

    public ImageView1v1(Context context) {
        super(context);
    }

    public ImageView1v1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView1v1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int newHeight = Math.round(width);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));

    }
}
