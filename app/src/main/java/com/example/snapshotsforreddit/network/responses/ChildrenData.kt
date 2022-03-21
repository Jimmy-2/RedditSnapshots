package com.example.snapshotsforreddit.network.responses

import com.example.snapshotsforreddit.network.responses.thumbnail.ImagePreview
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
    val is_self: Boolean,
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
    val title: String?,

    @Json(name = "selftext") val selfText: String?,

    @Json(name = "permalink") val permaLink: String?,

    val score: Int?,

    @Json(name = "num_comments") val numComments: Int?,


    //if true, the post is a text post
    @Json(name = "is_self") val isSelf: Boolean?,

    //This object contains the high res images/thumbnails
    val preview: ImagePreview?,

    //use preview for higher res. and if thumbnail == "" or "self", display text instead
    val thumbnail: String?
    )

