package com.example.snapshotsforreddit.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/*
        "kind": "t3",
        "data": {
 */


@JsonClass(generateAdapter = true)
data class ChildrenObject(
    @Json(name = "kind")
    val kind: String?,

    @Json(name = "data")
    val data: ChildrenData?

)





