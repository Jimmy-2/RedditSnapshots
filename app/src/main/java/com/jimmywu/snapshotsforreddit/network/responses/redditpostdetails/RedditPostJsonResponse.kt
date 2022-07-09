package com.jimmywu.snapshotsforreddit.network.responses.redditpostdetails

// Post json response will show a list of RedditPostCommentsDetails. The size will always be 2.
// The first item will contain details about the post itself and the 2nd item will contain details about the comments and replies in the post.
//data class RedditPostJsonResponse(val redditPostDetails: List<RedditPostCommentsDetail>)

data class RedditPostCommentsDetail(val kind: String?, val data: RedditPostCommentsData)


data class RedditPostCommentsData(
    val after: String?,
    val dist: Int?,
    val modhash: String?,
    val geo_filter: String?,
    val children: List<RedditPostChildrenObject>,
    val before: String?
)

data class RedditPostChildrenObject(val kind: String?, val data: RedditPostChildrenData)


