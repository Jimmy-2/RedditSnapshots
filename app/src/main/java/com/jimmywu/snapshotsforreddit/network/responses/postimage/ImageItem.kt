package com.jimmywu.snapshotsforreddit.network.responses.postimage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
//contains a source object and a list of resolution objects
data class ImageItem (
    val source: SourceObject?
): Parcelable
