package com.jimmywu.snapshotsforreddit.network.responses


data class RedditData(
    val after: String?,
    val dist: Int?,
    val modhash: String?,
    val geo_filter: String?,
    val children: List<RedditChildrenObject>,
    val before: String?
)


/*
    "after": "t3_orfxuf",
    "dist": 100,
    "modhash": "v0gnm5htnda7019468388365c4bdf869078ccdf53cc570d5da",
    "geo_filter": "",
    "children": [


 */


