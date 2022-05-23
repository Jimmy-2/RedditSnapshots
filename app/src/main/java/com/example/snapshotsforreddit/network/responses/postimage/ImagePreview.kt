package com.example.snapshotsforreddit.network.responses.postimage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ImagePreview(

    val images: List<ImageItem>?,

    val enabled: Boolean?
): Parcelable


