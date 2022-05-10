package com.jetpack_imooc.model

/**
 * @Title: BottomBar
 * @Package
 * @Description: 底部TabBar
 * @author dhl
 * @date 2022 0425
 * @version V1.0
 */
data class BottomBar(
    val activeColor: String,
    val inActiveColor: String,
    val selectTab: Int,
    val tabs: List<Tab>
)

data class Tab(
    val enable: Boolean,
    val index: Int,
    val pageUrl: String,
    val size: Int,
    val tintColor: String,
    val title: String
)