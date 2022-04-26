package com.example.snapshotsforreddit.network.responses.subreddit


data class SubredditData (
    val after: String?,
    //val dist: Int?,
    //val modhash: String?,
    //val geo_filter: String?,
    val children: List<SubredditChildrenObject>,
    val before: String?
)
