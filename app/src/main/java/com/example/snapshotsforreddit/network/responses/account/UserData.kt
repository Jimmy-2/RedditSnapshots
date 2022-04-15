package com.example.snapshotsforreddit.network.responses.account

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData (
    val name: String?,
    val link_karma: Int?,
    val comment_karma: Int?,
    val total_karma: Int?
    ) : Parcelable
