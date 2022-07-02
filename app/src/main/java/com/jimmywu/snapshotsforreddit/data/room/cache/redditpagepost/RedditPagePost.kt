package com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Entity


@Entity(tableName = "reddit_page_posts", primaryKeys = ["name", "redditPageNameAndSortOrder"])
data class RedditPagePost(
    //DEFAULTS
    val dataKind: String? = null,
    val isDefault: Boolean? = null,
    val isCompact: Boolean? = null,


    val redditPageNameAndSortOrder: String,
    val redditPageSortOrder: String,
    val redditPageName: String,
    //val position: Int,

    //FROM API RESPONSE
    val subreddit: String? = null,

    val selftext: String? = null,

    val saved: Boolean? = null,

    //true = post upvoted, false = post downvoted, null = neither
    //declare as var since we need to update its value when upvoting/downvoting on client side without having to recall api to fetch new changes
    //TODO CHANGE TO VAL AFTER CONVERTING TO CACHING
    val likes: Boolean? = null,

    val title: String? = null,

    val subreddit_name_prefixed: String? = null,

    val is_reddit_media_domain: Boolean? = null,

    val score: Int? = null,

    //use preview for higher res. and if thumbnail == "" or "self", display text instead
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
    //TODO
//    val preview: ImagePreview? = null,

    //used to get post details information
    val id: String? = null,

    //kind+  id

    val name: String,

    //author of post
    val author: String? = null,


    val num_comments: Int? = null,

    //contains media items related to video/gif
    //TODO: Map values from response into new variables that will be created here
//    val media: RedditPageMedia? = null,

    //contains data that you can get from a subreddit's /about endpoint
    //use this to get subreddit icon
    //TODO
//    val sr_detail: SubredditChildrenData? = null,

    //not used atm
    val permalink: String? = null,


    val is_video: Boolean? = null,

    val body: String? = null,

    val link_title: String? = null,


    val created_utc: Long? = null,


    //mapped content
    val previewUrl: String? = null,
    val previewWidth: Int? = null,
    val previewHeight: Int? = null,

    val sr_community_icon: String? = null,
    val sr_icon_img: String? = null,

//        media: RedditPageMedia


    //DEFAULTS
    val history_type: String? = null,
    val history_name: String? = null,
    val icon: Int? = null,

    //userinfo
    val user_name: String? = null,
    val link_karma: Int? = null,
    val comment_karma: Int? = null,
    val total_karma: Int? = null,
    val awarder_karma: Int? = null,
    val awardee_karma: Int? = null,
    val user_created_utc: Long? = null,


    val queryPosition: Int
//    var indexInResponse: Int = -1


)