package com.example.snapshotsforreddit.network.responses

import android.os.Parcelable
import com.example.snapshotsforreddit.network.responses.account.UserData
import com.example.snapshotsforreddit.network.responses.postimage.ImagePreview
import com.example.snapshotsforreddit.network.responses.postvideo.RedditPageMedia
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

//Contains everything relevant to a single post.
//Will not be using a majority of the keys in the json.
//see example in json format https://api.reddit.com/

/*

data class RedditChildrenData(
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
//Parcelize so that we can pass this object through safe args.
@Parcelize
@JsonClass(generateAdapter = true)
data class RedditChildrenData(
    val subreddit: String?,

    val selftext: String?,

    val saved: Boolean?,

    val title: String?,

    val subreddit_name_prefixed: String?,

    val is_reddit_media_domain: Boolean?,

    val score: Int?,

    //use preview for higher res. and if thumbnail == "" or "self", display text instead
    val thumbnail: String?,

    //tells us if post is an image, gif, video, or text post
    //hint == 'hosted:video' #reddit hosted video
    //hint == 'rich:video' # i.e. youtube
    //however some text only subreddits do not have post_hint
    val post_hint: String?,

    //if true, the post is a text post
    val is_self: Boolean?,

    //true = post upvoted, false = post downvoted, null = neither
    val likes: Boolean?,


    //url to image if post is only image
    val url_overridden_by_dest: String?,


    val archived: Boolean?,

    val no_follow: Boolean?,

    val is_crosspostable: Boolean?,

    val over_18: Boolean?,

    //This object contains the high res images/thumbnails
    val preview: ImagePreview?,

    //used to get post details information
    val id: String?,

    //author of post
    val author: String?,


    val num_comments: Int?,

    //contains media items related to video/gif
    val media: RedditPageMedia?,


    //not used atm
    val permalink: String?,


    val is_video: Boolean?,

    val body: String?,

    val link_title: String?,


    val created_utc: Long?,


    val userData: UserData?,
    ) : Parcelable

