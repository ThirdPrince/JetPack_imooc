package com.jetpack_imooc.model

import androidx.databinding.BaseObservable
import java.io.Serializable

/**
 * @Title: $
 * @Package $
 * @Description: Ugc
 * @author dhl
 * @date 2022 0425
 * @version V1.0
 */
data class Ugc(
    val commentCount: Int,
    val hasDissed: Boolean,
    val hasFavorite: Boolean,
    val hasLiked: Boolean,
    val hasdiss: Boolean,
    val likeCount: Int,
    val shareCount: Int
): BaseObservable(),Serializable{

}