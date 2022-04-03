package com.example.snapshotsforreddit.network.responses.subscribed

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscribedChildrenData(


    val display_name: String?,

    val display_name_prefixed: String?,

    val community_icon: String?,
    val icon_img: String?,

    val subreddit_type: String?,

    val user_has_favorited: Boolean?
)
