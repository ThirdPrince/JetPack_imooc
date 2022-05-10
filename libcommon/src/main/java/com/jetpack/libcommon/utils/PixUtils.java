package com.jetpack.libcommon.utils;

import android.util.DisplayMetrics;

/**
 * @author dhl
 * @version V1.0
 * @Title: PixUtils
 * @Package
 * @Description: PixUtils
 * @date 2022 0426
 */
public class PixUtils {

    /**
     * dp to px
     * @param dpValue
     * @return
     */
    public static int dp2px(int dpValue){
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return (int)(metrics.density* dpValue +0.5f);
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenWidth(){
       DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
       return metrics.widthPixels;
    }

    public static int getScreenHeight(){
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
