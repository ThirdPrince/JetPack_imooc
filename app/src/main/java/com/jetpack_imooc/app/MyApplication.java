package com.jetpack_imooc.app;

import android.app.Application;

import com.blankj.utilcode.util.CrashUtils;
import com.jetpack.libnetwork.net.ApiService;

/**
 * @author dhl
 * @version V1.0
 * @Title: MyApplication
 * @Package MyApplication
 * @Description: MyApplication
 * @date 2022 04 26
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init("http://123.56.232.18:8080/serverdemo", null);
        CrashUtils.init();
    }
}
