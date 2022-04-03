package com.example.snapshotsforreddit.network.responses.postvideo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RedditPageMedia (
    val oembed: Oembed?,

    val type: String?,

    val reddit_video: RedditVideo?


) : Parcelable
