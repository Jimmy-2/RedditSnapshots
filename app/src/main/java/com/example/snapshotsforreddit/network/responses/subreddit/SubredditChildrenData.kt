package com.example.snapshotsforreddit.network.responses.subreddit

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "subscribed_subreddits" )
data class SubredditChildrenData(
    //defaults
    val dataKind: String?,



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

    @PrimaryKey val name: String?,

    )
