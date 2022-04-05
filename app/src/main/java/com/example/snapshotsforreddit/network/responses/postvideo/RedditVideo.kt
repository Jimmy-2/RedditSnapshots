package com.example.snapshotsforreddit.network.responses.postvideo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RedditVideo (

    //link to gif/video
    val fallback_url: String?,

    val duration: Int?,
) : Parcelable