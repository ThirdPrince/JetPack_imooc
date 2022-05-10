package com.jetpack_imooc.model

/**
 * @Title: Destination
 * @Package com.jetpack_imooc.model
 * @Description: Destination 对象
 * @author dhl
 * @date 2022 04 25
 * @version V1.0
 */
data class Destination(
    val asStarter: Boolean,
    val className: String,
    val id: Int,
    val isFragment: Boolean,
    val needLogin: Boolean,
    val pageUrl: String
)