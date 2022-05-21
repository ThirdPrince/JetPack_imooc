package com.jetpack.libcommon.view;

import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.jetpack.libcommon.R;

/**
 * @author dhl
 * @version V1.0
 * @Title: ViewHelper
 * @Package $
 * @Description: ViewHelper
 * @date 2022 0521
 */
public class ViewHelper {

    public static final int RADIUS_ALL = 0;

    public static final int RADIUS_LEFT = 1;

    public static final  int RADIUS_TOP = 2;

    public static final  int RADIUS_RIGHT = 3;

    public static final int RADIUS_BOTTOM = 4;

    public static void setViewOutline(View view, AttributeSet attributeSet,int defStyleAttr,int defStyleRes){
        TypedArray array = view.getContext().obtainStyledAttributes(attributeSet, R.styleable.viewOutLineStrategy,defStyleAttr,defStyleRes);
        int radius = array.getDimensionPixelSize(R.styleable.viewOutLineStrategy_clip_radius,0);
        int hideSide = array.getInt(R.styleable.viewOutLineStrategy_clip_side,0);
        array.recycle();
        setViewOutLine(view,radius,hideSide);
    }

    public static void setViewOutLine(View view,final int radius,final int radiusSide){
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int w = view.getWidth(),h = view.getHeight();
                if( w ==0 ||  h== 0){
                    return;
                }
                if(radiusSide != RADIUS_ALL){
                    int left = 0,top = 0,right = w,bottom = h;
                    if(radiusSide == RADIUS_LEFT){
                        right += radius;
                    }else if(radiusSide == RADIUS_TOP){
                        bottom += radius;
                    }else if(radiusSide == RADIUS_RIGHT){
                        left -= radius;
                    }else if(radiusSide == RADIUS_BOTTOM){
                        top -= radius;
                    }
                    outline.setRoundRect(left,top,right,bottom,radius);
                    return;
                }

                int left = 0,top = 0,right = w,bottom = h;
                if(radius <= 0){
                    outline.setRect(left,top,right,bottom);
                }else{
                    outline.setRoundRect(left,top,right,bottom,radius);
                }


            }
        });
        view.setClipToOutline(radius > 0);
        view.invalidate();
    }

}
