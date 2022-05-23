package com.example.snapshotsforreddit.network.responses

import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.squareup.moshi.Json





data class RedditChildrenObject(
    @Json(name = "kind")
    val kind: String?,

    @Json(name = "data")
    val data: RedditChildrenData,

    val defaults: Defaults?,

)

//store hard coded values

data class Defaults (val type: String?, val text: String?, val userInfo: UserInfo?, val icon:Int? = null, val isCompact: Boolean? = null)





