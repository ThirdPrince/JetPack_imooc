package com.jetpack_imooc.model

import java.io.Serializable

/**
 * @Title: User
 * @Package $
 * @Description: User
 * @author dhl
 * @date 2022 0425
 * @version V1.0
 */
data class User(
    val avatar: String,
    val commentCount: Int,
    val description: String,
    val expires_time: Long,
    val favoriteCount: Int,
    val feedCount: Int,
    val followCount: Int,
    val followerCount: Int,
    val hasFollow: Boolean,
    val historyCount: Int,
    val id: Int,
    val likeCount: Int,
    val name: String,
    val qqOpenId: String,
    val score: Int,
    val topCommentCount: Int,
    val userId: Int,
    val user: User,
    val ugc: Ugc

):Serializable