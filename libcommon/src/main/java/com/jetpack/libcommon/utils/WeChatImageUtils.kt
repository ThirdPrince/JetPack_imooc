package com.jetpack.libcommon.utils

import com.jetpack.libcommon.image.ImageSize


/**
 * @ClassName WechatImageUtils
 * @Description 高仿微信的一种 图片宽高比
 * @Author dhl
 * @Date 2020/12/31 11:07
 * @Version 1.0
 */
object WeChatImageUtils {


    @JvmStatic
    fun getImageSizeByOrgSizeToWeChat( outWidth:Int,  outHeight:Int): ImageSize {
        val imageSize = ImageSize()
        val maxWidth = 400
        val maxHeight = 400
        val minWidth = 300
        val minHeight = 250
        if (outWidth / maxWidth > outHeight / maxHeight) { //
            if (outWidth >= maxWidth) { //
                imageSize.width = maxWidth
                imageSize.height = outHeight * maxWidth / outWidth
            } else {
                imageSize.width = outWidth
                imageSize.height = outHeight
            }
            if (outHeight < minHeight) {
                imageSize.height = minHeight
                val width: Int = outWidth * minHeight / outHeight
                if (width > maxWidth) {
                    imageSize.width = maxWidth
                } else {
                    imageSize.width = width
                }
            }
        } else {
            if (outHeight >= maxHeight) {
                imageSize.height = maxHeight
                if (outHeight / maxHeight > 10) {
                    imageSize.width = outWidth * 5 * maxHeight / outHeight
                } else {
                    imageSize.width = outWidth * maxHeight / outHeight
                }
            } else {
                imageSize.height = outHeight
                imageSize.width = outWidth
            }
            if (outWidth < minWidth) {
                imageSize.width = minWidth
                val height: Int = outHeight * minWidth / outWidth
                if (height > maxHeight) {
                    imageSize.height = maxHeight
                } else {
                    imageSize.height = height
                }
            }
        }

        return imageSize
    }
}