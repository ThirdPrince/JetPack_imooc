package com.jetpack.libcommon.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.DataOutputStream

/**
 * @Title: dhl
 * @Package
 * @Description: ToastUtils
 * @author dhl
 * @date 2022 04 28
 * @version V1.0
 */
object ToastUtils {

    @JvmStatic
    fun show(msg:String){
        if( Looper.getMainLooper() == Looper.myLooper()){
            Toast.makeText(AppGlobals.application,msg,Toast.LENGTH_SHORT).show()
        }else{
            Handler(Looper.getMainLooper()).post {
                fun run() {
                    Toast.makeText(AppGlobals.application, msg, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}