package com.example.snapshotsforreddit.network.responses.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo (
    val name: String?,
    val link_karma: Int?,
    val comment_karma: Int?,
    val total_karma: Int?,

    val created_utc: Long?
    ) : Parcelable
