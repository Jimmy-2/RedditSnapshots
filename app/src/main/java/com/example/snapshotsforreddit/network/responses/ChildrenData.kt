package com.example.snapshotsforreddit.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Contains everything relevant to a single post.
//Will not be using a majority of the keys in the json.
//see example in json format https://api.reddit.com/

/*

data class ChildrenData(
    val subreddit: String?,
    val selftext: String?,
    val author_fullname: String?,
    val saved: Boolean,
    val title: String?,
    val name: String?,
    val score: Int?,
    val thumbnail: String?,
    val subreddit_id: String?,
    val id: String?,
    val author: String?,
    val num_comments: Int?,
    val permalink: String?,
    val url: String?,
    val is_video: Boolean?
)


 */
@JsonClass(generateAdapter = true)
data class ChildrenData (
    @Json(name = "title") val title: String?,

    @Json(name = "permalink") val permalink: String?,

    @Json(name = "score") val score: Int?,

    @Json(name = "num_comments") val num_comments: Int?,

    //use preview for higher res. and if thumbnail == "" or "self", display text instead
    @Json(name = "thumbnail") val thumbnail: String?
    )

