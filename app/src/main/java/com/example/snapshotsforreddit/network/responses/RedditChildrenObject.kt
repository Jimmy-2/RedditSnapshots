package com.example.snapshotsforreddit.network.responses

import com.squareup.moshi.Json

data class RedditChildrenObject(
    @Json(name = "kind")
    val kind: String?,

    @Json(name = "data")
    val data: RedditChildrenData,


)





