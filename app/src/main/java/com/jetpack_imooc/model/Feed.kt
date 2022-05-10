package com.jetpack_imooc.model

import java.io.Serializable

/**
 * @Title: Feed
 * @Package
 * @Description:Feed 帖子
 * @author dhl
 * @date 2022 0425
 * @version V1.0
 */
@JvmField
val TYPE_IMAGE_TEXT = 1 //图文
@JvmField
val TYPE_VIDEO = 2 //视频

data class Feed (
        val activityIcon: String,
        val activityText: String,
        val authorId: Int,
        val cover: String,
        val createTime: Long,
        val duration: Int,
        val feeds_text: String,
        val height: Int,
        val id: Int,
        val itemId: Long,
        val itemType: Int,
        val url: String,
        val width: Int,
        val ugc: Ugc,
        val author: User,
        val topComment: Comment
):Serializable