package com.jetpack.libcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jetpack.libcommon.R;

/**
 * TODO: document your custom view class.
 */
public class CornerFrameLayout extends FrameLayout {


    public CornerFrameLayout(Context context) {
        this(context,null);

    }

    public CornerFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public CornerFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle,0);

    }
    public CornerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ViewHelper.setViewOutline(this, attrs, defStyleAttr, defStyleRes);
    }

    public  void setViewOutline(int radius,int radiusSide){
       ViewHelper.setViewOutLine(this,radius,radiusSide);
    }


}