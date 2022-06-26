package com.example.snapshotsforreddit.network.responses

import com.example.snapshotsforreddit.network.responses.postimage.ImagePreview
import com.example.snapshotsforreddit.network.responses.postvideo.RedditPageMedia
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData


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
//@Entity(tableName = "redditChildrenObjects")


data class RedditChildrenData(

    //DEFAULTS
    val dataKind: String? = null,
    val isDefault: Boolean? = null,
    val isCompact: Boolean? = null,




    //FROM API RESPONSE
    val subreddit: String? = null,

    val selftext: String? = null,

    val saved: Boolean? = null,

    //true = post upvoted, false = post downvoted, null = neither
    //declare as var since we need to update its value when upvoting/downvoting on client side without having to recall api to fetch new changes
    var likes: Boolean? = null,

    val title: String? = null,

    val subreddit_name_prefixed: String? = null,

    val is_reddit_media_domain: Boolean? = null,

    val score: Int? = null,

//    //use preview for higher res. and if thumbnail == "" or "self", display text instead
    val thumbnail: String? = null,

    //tells us if post is an image, gif, video, or text post
    //hint == 'hosted:video' #reddit hosted video
    //hint == 'rich:video' # i.e. youtube
    //however some text only subreddits do not have post_hint
//    val post_hint: String?,

    //if true, the post is a text post
    val is_self: Boolean? = null,

    //url to image if post is only image
    val url_overridden_by_dest: String? = null,


    val archived: Boolean? = null,

    val no_follow: Boolean? = null,

    val is_crosspostable: Boolean? = null,

    val over_18: Boolean? = null,

    //This object contains the high res images/thumbnails
    val preview: ImagePreview? = null,

    //used to get post details information
    val id: String? = null,

    //kind+  id
    val name: String,

    //author of post
    val author: String? = null,


    val num_comments: Int? = null,

    //contains media items related to video/gif
    val media: RedditPageMedia? = null,

    //contains data that you can get from a subreddit's /about endpoint
    //use this to get subreddit icon
    val sr_detail: SubredditChildrenData? = null,

    //not used atm
    val permalink: String? = null,


    val is_video: Boolean? = null,

    val body: String? = null,

    val link_title: String? = null,


    val created_utc: Long? = null,








    //DEFAULTS
    val history_type : String? = null,
    val history_name: String? = null,
    val icon:Int? = null,

    //userinfo
    val user_name: String? = null,
    val link_karma: Int? = null,
    val comment_karma: Int? = null,
    val total_karma: Int? = null,
    val awarder_karma: Int? = null,
    val awardee_karma: Int? = null,
    val user_created_utc: Long? = null






    )

