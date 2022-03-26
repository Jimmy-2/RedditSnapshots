package com.example.snapshotsforreddit.network.responses.thumbnail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
//contains a source object and a list of resolution objects
data class ImageItem (
    val source: SourceObject?
): Parcelable
