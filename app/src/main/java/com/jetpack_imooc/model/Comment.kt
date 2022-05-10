package com.jetpack_imooc.model

import java.io.Serializable

/**
 * @Title: Comment
 * @Package
 * @Description: Comment
 * @author dhl
 * @date 2022 0426
 * @version V1.0
 */
data class Comment(
    val author: User,
    val commentCount: Int,
    val commentId: Long,
    val commentText: String,
    val commentType: Int,
    val createTime: Long,
    val hasLiked: Boolean,
    val height: Int,
    val id: Int,
    val imageUrl: String,
    val itemId: Long,
    val likeCount: Int,
    val ugc: Ugc,
    val userId: Int,
    val videoUrl: String,
    val width: Int
):Serializable



