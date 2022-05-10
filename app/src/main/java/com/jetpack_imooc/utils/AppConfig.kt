package com.jetpack_imooc.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.jetpack.libcommon.utils.AppGlobals
import com.jetpack_imooc.model.BottomBar
import com.jetpack_imooc.model.Destination
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * @Title: AppConfig
 * @Package
 * @Description: AppConfig
 * @author dhl
 * @date 2022 04 25
 * @version V1.0
 */
object AppConfig {

    private const val APP_DESTINATION = "destination.json"

    private const val BOTTOM_BAR = "main_tabs_config.json"

    /**
     *  destination 参数类型
     */
    private val desListType = object : TypeToken<Map<String, Destination>>() {}.type

    /**
     *  destination 参数类型
     */
    private val bottomBarType = object : TypeToken<BottomBar>() {}.type


    @JvmStatic
    val sDesConfig: Map<String, Destination> by lazy {
        GsonUtils.fromJson(getJson(APP_DESTINATION), desListType)
    }

    @JvmStatic
    val sBottomBar:BottomBar by  lazy {
        GsonUtils.fromJson(getJson(BOTTOM_BAR), bottomBarType)
    }


    /**
     * json 解析
     */
    private fun getJson(fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = AppGlobals.application.assets
            val bf = BufferedReader(InputStreamReader(
                    fileName?.let {
                        assetManager.open(it)
                    }))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


}