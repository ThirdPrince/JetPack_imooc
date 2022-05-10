package com.jetpack.libcommon.utils

import android.app.Application

/**
 * @Title: AppGlobals
 * @Package $
 * @Description: application 全局对象
 * @author dhl
 * @date 2022 04 24
 * @version V1.0
 */
object AppGlobals {

    @JvmStatic
    val application: Application by lazy {

        Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication")
                .invoke(null, *(arrayOfNulls<Any>(0))) as Application
    }


}