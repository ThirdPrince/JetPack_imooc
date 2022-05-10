package com.jetpack.libcommon.utils

import android.util.Log
import com.jetpack.libcommon.BuildConfig

/**
 * @ClassName EasyLogUtil
 * @Description 日志规范
 * @Author dhl
 * @Date 2021/1/26 14:27
 * @Version 1.0
 */
object EasyLog {

    @JvmStatic
    fun v( tag:String, msg:String){
        if(BuildConfig.DEBUG){
            Log.v(tag,msg)
        }
    }

    @JvmStatic
    fun d( tag:String, msg:String){
        if(BuildConfig.DEBUG){
            Log.d(tag,msg)
        }
    }

    @JvmStatic
    fun i( tag:String, msg:String){
        if(BuildConfig.DEBUG){
            Log.i(tag,msg)
        }
    }

    @JvmStatic
    fun w( tag:String, msg:String){
        if(BuildConfig.DEBUG){
            Log.w(tag,msg)
        }
    }
    @JvmStatic
    fun e( tag:String, msg:String){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg)
        }
    }
}