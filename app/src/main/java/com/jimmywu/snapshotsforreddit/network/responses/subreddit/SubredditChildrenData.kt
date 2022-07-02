package com.jimmywu.snapshotsforreddit.network.responses.subreddit



data class SubredditChildrenData(
    //defaults
    val dataKind: String?,

    val sortPriority: Int = 2,

    //from api response
    val display_name: String?,

    val display_name_prefixed: String?,

    val subscribers: Int?,

    val created_utc: Long?,


    val community_icon: String?,

    val primary_color: String?,

    val icon_img: String?,

    val subreddit_type: String?,

    val user_has_favorited: Boolean?,

    val public_description: String?,

    //unique id
    val name: String,

    )
