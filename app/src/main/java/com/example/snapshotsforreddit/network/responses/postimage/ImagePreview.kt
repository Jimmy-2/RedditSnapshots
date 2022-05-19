package com.example.snapshotsforreddit.network.responses.postimage

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class ImagePreview(

    val images: List<ImageItem>?,

    val enabled: Boolean?
): Parcelable


