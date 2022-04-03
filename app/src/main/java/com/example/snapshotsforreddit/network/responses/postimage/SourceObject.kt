package com.example.snapshotsforreddit.network.responses.postimage

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
    "source": {
        "url": "https://preview.redd.it/6srcmbgg46o81.jpg?auto=webp&amp;s=a6380d75101a13b02207b1363e8f616a48d22e3b",
        "width": 880,
        "height": 682
    }
 */
@Parcelize
data class SourceObject(
    val url: String?,
    val width: Int?,
    val height: Int?
): Parcelable
