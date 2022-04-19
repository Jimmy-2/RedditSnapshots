package com.example.snapshotsforreddit.network.responses

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


/*
        "kind": "t3",
        "data": {
 */


@Parcelize
@JsonClass(generateAdapter = true)
data class RedditChildrenObject(
    @Json(name = "kind")
    val kind: String?,

    @Json(name = "data")
    val data: RedditChildrenData?

) : Parcelable





