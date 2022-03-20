package com.example.snapshotsforreddit.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ImagePreview(

    val images: List<ImageItem>?,

    val enabled: Boolean?
)

