package com.example.snapshotsforreddit.network.responses.thumbnail

import com.example.snapshotsforreddit.network.responses.SourceObject


//contains a source object and a list of resolution objects
data class ImageItem (
    val source: SourceObject?
)
