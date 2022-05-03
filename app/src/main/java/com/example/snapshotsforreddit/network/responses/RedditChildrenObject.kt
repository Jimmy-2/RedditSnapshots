package com.example.snapshotsforreddit.network.responses

import android.os.Parcelable
import com.example.snapshotsforreddit.network.responses.account.UserInfo
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
    val data: RedditChildrenData?,

    val defaults: Defaults?,

) : Parcelable

//store hard coded values
@Parcelize
data class Defaults (val type: String?, val text: String?, val userInfo: UserInfo?, val icon:Int? = null, val isCompact: Boolean? = null) : Parcelable





